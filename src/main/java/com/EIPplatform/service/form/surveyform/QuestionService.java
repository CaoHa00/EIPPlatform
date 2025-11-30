package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.mapper.form.surveyform.QuestionMapper;
import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.EditQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionDTO;
import com.EIPplatform.model.entity.form.surveyform.*;
import com.EIPplatform.repository.form.surveyform.QuestionCategoryRepository;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;
import com.EIPplatform.service.form.submission.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionCategoryRepository questionCategoryRepository;
    private final SurveySecurityService securityService;
    private final QuestionOptionService optionService;
    private final SubmissionService submissionService; // replace this with submission service later
    private final ExceptionFactory exceptionFactory;
    private final QuestionMapper questionMapper;

    public QuestionService(QuestionRepository questionRepository, QuestionCategoryRepository questionCategoryRepository, SurveySecurityService securityService,
                           QuestionOptionService optionService, SubmissionService submissionService, ExceptionFactory exceptionFactory, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionCategoryRepository = questionCategoryRepository;
        this.securityService = securityService;
        this.optionService = optionService;
        this.submissionService = submissionService;
        this.exceptionFactory = exceptionFactory;
        this.questionMapper = questionMapper;
    }

    public QuestionDTO getQuestion(UUID id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", id, FormError.QUESTION_NOT_FOUND));

        return questionMapper.toDTO(question);
    }

    @Transactional
    public void reorderQuestions(UUID surveyId, ReorderRequestDTO dto) {
        //creator check
        SurveyForm form = securityService.getFormIfCreator(surveyId);

        List<Question> questions = form.getQuestions();

        Map<UUID, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        List<UUID> newOrder = dto.getOrderedIds();

        for (int i = 1; i <= newOrder.size(); i++) {
            UUID questionId = newOrder.get(i-1);

            // only update if the question actually exists in this form
            if (questionMap.containsKey(questionId)) {
                Question question = questionMap.get(questionId);

                // only update if the order actually changed (optimization)
                if (question.getDisplayOrder() != i) {
                    question.setDisplayOrder(i);
                }
            }
        }

        // update updatedAt
        form.setUpdatedAt(LocalDateTime.now());

        // hibernate handles update
    }

    //edit question text and the boolean required
    @Transactional
    public QuestionDTO editQuestion(UUID questionId, EditQuestionDTO dto) {
        // creator check
        Question question = securityService.getQuestionIfCreator(questionId);

        if (dto.getText() != null && !dto.getText().isBlank()) {
            question.setText(dto.getText());
        }

        if (dto.getRequired() != null) {
            question.setRequired(dto.getRequired());
        }

        if (dto.getCategoryId() != null) {
            QuestionCategory category = questionCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionCategory", "id", dto.getCategoryId(), FormError.QUESTION_CATEGORY_NOT_FOUND));
            question.setQuestionCategory(category);
        }

        // update updatedAt property
        if (question.getSurveyForm() != null) {
            question.getSurveyForm().setUpdatedAt(LocalDateTime.now());
        }

        return questionMapper.toDTO(questionRepository.save(question));
    }

    /**
     * Helper: Builds the entity (and children) WITHOUT saving.
     * Used by SurveyService for bulk operations.
     */
    public Question buildQuestionEntity(CreateQuestionDTO dto, SurveyForm parent) {
        Question question = new Question();
        question.setText(dto.getText());
        question.setType(QuestionType.valueOf(dto.getType()));
        question.setDisplayOrder(dto.getDisplayOrder());
        question.setRequired(dto.getRequired() != null && dto.getRequired());
        question.setSurveyForm(parent);
        question.setActive(true);

        if (dto.getCategoryId() != null) {
            QuestionCategory category = questionCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("QuestionCategory", "id", dto.getCategoryId(), FormError.QUESTION_CATEGORY_NOT_FOUND));
            question.setQuestionCategory(category);
        }

        // delegate, let QuestionOptionService create QuestionOption
        if (dto.getOptions() != null) {
            List<QuestionOption> options = dto.getOptions().stream()
                    .map(oDto -> optionService.buildOptionEntity(oDto, question))
                    .collect(Collectors.toList());

            question.setOptions(options);
        }

        return question;
    }

    /**
     * Granular: Builds AND Saves.
     */
    @Transactional
    public QuestionDTO addQuestion(CreateQuestionDTO dto, UUID parentId){
        SurveyForm parent = securityService.getFormIfCreator(parentId);

        //validate displayOrder
        Optional<Question> existingDisplayOrder = questionRepository.findByDisplayOrderAndActiveTrue(dto.getDisplayOrder());
        existingDisplayOrder.ifPresent(e -> {
                    throw exceptionFactory.createAlreadyExistsException("Question", "displayOrder", dto.getDisplayOrder(), ValidationError.DISPLAY_ORDER_ALREADY_EXISTS);
                }
        );

        //save question
        Question question = buildQuestionEntity(dto, parent);
        QuestionDTO savedQuestionDTO = questionMapper.toDTO(questionRepository.save(question));

        //updatedAt
        parent.setUpdatedAt(LocalDateTime.now());
        return savedQuestionDTO;
    }

    @Transactional
    public void activeSwitch(UUID id) {
        Question question = securityService.getQuestionIfCreator(id);

        boolean newActive = !question.isActive();
        question.setActive(newActive);
    }

    /**
     * Granular: Single delete for API Endpoint
     */
    @Transactional
    public void hardDeleteQuestion(UUID questionId){
        // Creator check
        Question question = securityService.getQuestionIfCreator(questionId);
        SurveyForm parentForm = question.getSurveyForm();

        // If submissions exist, deleting a question corrupts the data
        if (submissionService.getSubmissionCountOfSurvey(parentForm.getId()) > 0) {
            throw exceptionFactory.createCustomException("Question",
                    Collections.singletonList("submissionCount"),
                    Collections.singletonList(submissionService.getSubmissionCountOfSurvey(parentForm.getId())),
                    FormError.SUBMISSIONS_EXIST);
        }

        // DELEGATE: Cleanup Children
        optionService.batchDeleteOptions(question);

        // this if condition prevents Hibernate from trying to "Resave" the deleted options
        // because the entity has the CascadeType.MERGE/PERSIST rules
        if (question.getOptions() != null) {
            question.getOptions().clear();
        }

        questionRepository.delete(question);

        // update the updatedAt property
        parentForm.setUpdatedAt(LocalDateTime.now());
    }

    /**
     * Hard delete all questions for a specific form
     * DELEGATION: Called by SurveyService
     */
    @Transactional
    public void batchDeleteQuestions(SurveyForm form) {
        List<Question> questions = form.getQuestions();

        if (questions == null || questions.isEmpty()) {
            return;
        }

        //Delegation: clean up children
        for (Question question : questions) {
            optionService.batchDeleteOptions(question);
        }


        // Database delete
        questionRepository.deleteAllInBatch(questions);
    }

}
