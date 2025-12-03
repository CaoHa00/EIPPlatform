package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.mapper.form.surveyform.QuestionOptionMapper;
import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.option.CreateOptionDTO;
import com.EIPplatform.model.dto.form.surveyform.option.OptionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.DeleteQuestionDTO;
import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.QuestionOption;
import com.EIPplatform.repository.form.surveyform.QuestionOptionRepository;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class QuestionOptionService implements QuestionOptionServiceInterface {
    private final List<String> VALID_QUESTION_TYPES = List.of("MULTIPLE_CHOICE", "CHECKBOX", "DROPDOWN", "LIKERT", "YES_NO");
    private final QuestionOptionRepository optionRepository;
    private final SurveyAccessControlServiceInterface accessControlService;
    private final QuestionRepository questionRepository;
    private final ExceptionFactory exceptionFactory;
    private final QuestionOptionMapper questionOptionMapper;
    private final SurveyServiceInterface surveyService;

    @Override
    public OptionDTO getOption(UUID id) {
        QuestionOption option = optionRepository.findById(id).orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionOption", "id", id, FormError.QUESTION_OPTION_NOT_FOUND));
        return questionOptionMapper.toDTO(option);
    }

    /**
     * Helper: Builds the entity WITHOUT saving it.
     * Used by QuestionService for bulk operations.
     */
    @Override
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
    @Override
    public OptionDTO addOption(UUID questionId, CreateOptionDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        Question parentQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", questionId, FormError.QUESTION_NOT_FOUND));

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

        //update updatedAt - Needs to traverse via GroupDimension -> Dimension -> SurveyForm
        updateParentSurveyUpdatedAt(parentQuestion);
        return savedOptionDTO;
    }

    @Transactional
    @Override
    public OptionDTO editOption(String text, UUID optionId, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionOption", "id", optionId, FormError.QUESTION_NOT_FOUND));

        if (text != null && !text.isBlank()) {
            option.setText(text);
        }

        return questionOptionMapper.toDTO(option);
    }

    @Transactional
    @Override
    public void activeSwitch(UUID optionId, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionOption", "id", optionId, FormError.QUESTION_NOT_FOUND));

        option.setActive(!option.isActive());

        //update updatedAt property - Needs to traverse via GroupDimension -> Dimension -> SurveyForm
        updateParentSurveyUpdatedAt(option.getQuestion());
    }

    @Transactional
    @Override
    public void reorderOptions(UUID questionId, ReorderRequestDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", questionId, FormError.QUESTION_NOT_FOUND));

        List<QuestionOption> options = question.getOptions();

        if (options.size() != dto.getOrderedIds().size()){
            throw exceptionFactory.createValidationException("ReorderRequestDTO", "orderedIds.size()", dto.getOrderedIds().size(), ValidationError.INVALID_REORDER_REQUEST_SIZE);
        }

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

        //update updatedAt property - Needs to traverse via GroupDimension -> Dimension -> SurveyForm
        updateParentSurveyUpdatedAt(question);
    }

    /**
     * Batch Delete: Delete all Options in a Question
     */
    @Transactional
    @Override
    public void batchDeleteOptions(DeleteQuestionDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);


        List<UUID> optionsList = dto.getQuestionIdsList();
        List<QuestionOption> options = optionRepository.findAllById(optionsList);

        optionRepository.deleteAllInBatch(options);
    }

    /**
     * Granular single delete for API Endpoint
     */
    @Transactional
    @Override
    public void hardDeleteOption(UUID optionId, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        QuestionOption option = optionRepository.findById(optionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionOption", "id", optionId, FormError.QUESTION_NOT_FOUND));

        Question parentQuestion = option.getQuestion();

        optionRepository.delete(option);

        //update updatedAt property - Needs to traverse via GroupDimension -> Dimension -> SurveyForm
        updateParentSurveyUpdatedAt(parentQuestion);
    }

    private void updateParentSurveyUpdatedAt(Question question) {
        if (question != null &&
            question.getGroupDimension() != null &&
            question.getGroupDimension().getDimension() != null &&
            question.getGroupDimension().getDimension().getSurveyForm() != null) {
            surveyService.updateSurveyUpdatedAt(question.getGroupDimension().getDimension().getSurveyForm().getId());
        }
    }
}