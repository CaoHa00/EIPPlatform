package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.mapper.form.surveyform.QuestionOptionMapper;
import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.option.CreateOptionDTO;
import com.EIPplatform.model.dto.form.surveyform.option.OptionDTO;
import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.QuestionOption;
import com.EIPplatform.repository.form.surveyform.QuestionOptionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionOptionService {
    private final List<String> VALID_QUESTION_TYPES = List.of("MULTIPLE_CHOICE", "CHECKBOX", "DROPDOWN", "LIKERT");
    private final QuestionOptionRepository optionRepository;
    private final SurveySecurityService securityService;
    private final ExceptionFactory exceptionFactory;
    private final QuestionOptionMapper questionOptionMapper;

    public QuestionOptionService(QuestionOptionRepository optionRepository, SurveySecurityService securityService, ExceptionFactory exceptionFactory, QuestionOptionMapper questionOptionMapper) {
        this.optionRepository = optionRepository;
        this.securityService = securityService;
        this.exceptionFactory = exceptionFactory;
        this.questionOptionMapper = questionOptionMapper;
    }

    public OptionDTO getOption(UUID id) {
        QuestionOption option = optionRepository.findById(id).orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionOption", "id", id, FormError.QUESTION_OPTION_NOT_FOUND));
        return questionOptionMapper.toDTO(option);
    }

    /**
     * Helper: Builds the entity WITHOUT saving it.
     * Used by QuestionService for bulk operations.
     */
    public QuestionOption buildOptionEntity(CreateOptionDTO dto, Question parent) {
        QuestionOption option = new QuestionOption();
        option.setText(dto.getText());
        option.setDisplayOrder(dto.getDisplayOrder());
        option.setQuestion(parent);
        option.setActive(true);
        return option;
    }

    /**
     * Granular: Builds AND Saves.
     * Used for single additions via API.
     */
    @Transactional
    public OptionDTO addOption(UUID questionId, CreateOptionDTO dto) {
        Question parentQuestion = securityService.getQuestionIfCreator(questionId);

        //validate question type, short answers and paragraphs shouldn't have multiple options
        String parentQuestionType = parentQuestion.getType().toString();
        if (!VALID_QUESTION_TYPES.contains(parentQuestionType)) {
            throw exceptionFactory.createValidationException("Question", "type", parentQuestionType, ValidationError.INVALID_QUESTION_TYPE);
        }

        //validate displayOrder
        Optional<QuestionOption> existingDisplayOrder = optionRepository.findByDisplayOrderAndActiveTrue(dto.getDisplayOrder());
        existingDisplayOrder.ifPresent(e -> {
                    throw exceptionFactory.createAlreadyExistsException("QuestionOption", "displayOrder", dto.getDisplayOrder(), ValidationError.DISPLAY_ORDER_ALREADY_EXISTS);
                }
        );

        //create option
        QuestionOption option = buildOptionEntity(dto, parentQuestion);
        OptionDTO savedOptionDTO = questionOptionMapper.toDTO(optionRepository.save(option));

        //update updatedAt
        if (parentQuestion.getSurveyForm() != null) {
            parentQuestion.getSurveyForm().setUpdatedAt(LocalDateTime.now());
        }
        return savedOptionDTO;
    }

    @Transactional
    public OptionDTO editOption(String text, UUID optionId) {
        QuestionOption option = securityService.getOptionIfCreator(optionId);

        if (text != null && !text.isBlank()) {
            option.setText(text);
        }

        return questionOptionMapper.toDTO(option);
    }

    @Transactional
    public void activeSwitch(UUID optionId) {
        QuestionOption option = securityService.getOptionIfCreator(optionId);

        option.setActive(!option.isActive());

        //update updatedAt property
        if (option.getQuestion() != null && option.getQuestion().getSurveyForm() != null) {
            option.getQuestion().getSurveyForm().setUpdatedAt(LocalDateTime.now());
        }
    }

    @Transactional
    public void reorderOptions(UUID questionId, ReorderRequestDTO dto) {
        Question question = securityService.getQuestionIfCreator(questionId);

        List<QuestionOption> options = question.getOptions();
        Map<UUID, QuestionOption> optionMap = options.stream()
                .collect(Collectors.toMap(QuestionOption::getId, Function.identity()));

        List<UUID> newOrder = dto.getOrderedIds();
        for (int i = 1; i <= newOrder.size(); i++) {
            UUID optionId = newOrder.get(i-1);

            if (optionMap.containsKey(optionId)) {
                QuestionOption opt = optionMap.get(optionId);
                if (opt.getDisplayOrder() != i) {
                    opt.setDisplayOrder(i);
                }
            }
        }

        if (question.getSurveyForm() != null) {
            question.getSurveyForm().setUpdatedAt(LocalDateTime.now());
        }
    }

    /**
     * Batch Delete: Delete all Options in a Question
     * Used by QuestionService
     */
    @Transactional
    public void batchDeleteOptions(Question question) {
        // delete from database
        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            optionRepository.deleteAllInBatch(question.getOptions());
        }
    }

    /**
     * Granular single delete for API Endpoint
     */
    @Transactional
    public void hardDeleteOption(UUID optionId) {
        QuestionOption option = securityService.getOptionIfCreator(optionId);

        Question parentQuestion = option.getQuestion();

        optionRepository.delete(option);

        if (parentQuestion.getSurveyForm() != null) {
            parentQuestion.getSurveyForm().setUpdatedAt(LocalDateTime.now());
        }
    }
}
