package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.mapper.form.surveyform.QuestionMapper;
import com.EIPplatform.model.dto.form.surveyform.ReorderRequestDTO;
import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.DeleteQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.EditQuestionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionDTO;
import com.EIPplatform.model.entity.form.surveyform.*;
import com.EIPplatform.repository.form.surveyform.GroupDimensionRepository;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;
import com.EIPplatform.service.form.submission.SubmissionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService implements QuestionServiceInterface {

    private final QuestionRepository questionRepository;
    private final GroupDimensionRepository groupDimensionRepository;
    private final SurveyAccessControlServiceInterface accessControlService;
    private final QuestionOptionServiceInterface optionService;
    private final SubmissionServiceInterface submissionService;
    private final ExceptionFactory exceptionFactory;
    private final QuestionMapper questionMapper;
    private final SurveyServiceInterface surveyService;


    public QuestionDTO getQuestion(UUID id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", id, FormError.QUESTION_NOT_FOUND));

        return questionMapper.toDTO(question);
    }

    /**
     * Reorder every single question in a GroupDimension
     * @throws ValidationError if the request DTO does not send every QuestionID in a GroupDimension
     * @param groupDimensionId
     * @param dto
     */
    @Transactional
    @Override
    public void reorderQuestions(UUID groupDimensionId, ReorderRequestDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        GroupDimension groupDimension = groupDimensionRepository.findById(groupDimensionId).orElseThrow(() ->
                exceptionFactory.createNotFoundException("GroupDimension", "id", groupDimensionId, FormError.GROUP_DIMENSION_NOT_FOUND));

        Set<Question> questions = groupDimension.getQuestions();

        if (questions.size() != dto.getOrderedIds().size()){
            throw exceptionFactory.createValidationException("ReorderRequestDTO", "orderedIds.size()", dto.getOrderedIds().size(), ValidationError.INVALID_REORDER_REQUEST_SIZE);
        }

        Map<UUID, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        List<UUID> newOrder = dto.getOrderedIds();
        for (int i = 1; i <= newOrder.size(); i++) {
            UUID questionId = newOrder.get(i-1);

            if (questionMap.containsKey(questionId)) {
                Question q = questionMap.get(questionId);
                if (q.getDisplayOrder() != i) {
                    q.setDisplayOrder(i);
                }
            }
        }
    }

    //edit question text and the boolean required
    @Transactional
    @Override
    public QuestionDTO editQuestion(UUID questionId, EditQuestionDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", questionId, FormError.QUESTION_NOT_FOUND));

        if (dto.getText() != null && !dto.getText().isBlank()) {
            question.setText(dto.getText());
        }

        if (dto.getCode() != null && !dto.getCode().isBlank()) {
            question.setCode(dto.getCode());
        }

        if (dto.getRequired() != null) {
            question.setRequired(dto.getRequired());
        }

        if (dto.getGroupDimensionId() != null) {
            GroupDimension groupDimension = groupDimensionRepository.findById(dto.getGroupDimensionId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("GroupDimension", "id", dto.getGroupDimensionId(), FormError.GROUP_DIMENSION_NOT_FOUND));
            question.setGroupDimension(groupDimension);
        }

        // update updatedAt property
        updateParentSurveyUpdatedAt(question);

        return questionMapper.toDTO(questionRepository.save(question));
    }

    /**
     * Helper: Builds the entity (and children) WITHOUT saving.
     * Used by SurveyService for bulk operations.
     */
    @Override
    public Question buildQuestionEntity(CreateQuestionDTO dto) {
        Question question = new Question();
        question.setText(dto.getText());
        question.setCode(dto.getCode());
        question.setType(QuestionType.valueOf(dto.getType()));
        question.setDisplayOrder(dto.getDisplayOrder());
        question.setRequired(dto.getRequired() != null && dto.getRequired());
        question.setActive(true);

        if (dto.getGroupDimensionId() != null) {
            GroupDimension groupDimension = groupDimensionRepository.findById(dto.getGroupDimensionId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("GroupDimension", "id", dto.getGroupDimensionId(), FormError.GROUP_DIMENSION_NOT_FOUND));
            question.setGroupDimension(groupDimension);
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
    @Override
    public QuestionDTO addQuestion(CreateQuestionDTO dto, UUID userAccountId){
        accessControlService.ensureBecamexRole(userAccountId);
        
        // The question is linked via GroupDimension
        
        Question question = buildQuestionEntity(dto);

        QuestionDTO savedQuestionDTO = questionMapper.toDTO(questionRepository.save(question));

        // Update SurveyForm updatedAt if possible
        updateParentSurveyUpdatedAt(question);

        return savedQuestionDTO;
    }

    @Transactional
    @Override
    public void activeSwitch(UUID id, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", id, FormError.QUESTION_NOT_FOUND));

        boolean newActive = !question.isActive();
        question.setActive(newActive);
    }

    /**
     * Granular: Single delete for API Endpoint
     */
    @Transactional
    @Override
    public void hardDeleteQuestion(UUID questionId, UUID userAccountId){
        accessControlService.ensureBecamexRole(userAccountId);
        
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", questionId, FormError.QUESTION_NOT_FOUND));
        
        // Find SurveyForm to check submissions
        SurveyForm parentForm = null;
        if (question.getGroupDimension() != null && 
            question.getGroupDimension().getDimension() != null) {
            parentForm = question.getGroupDimension().getDimension().getSurveyForm();
        }

        if (parentForm != null) {
            // If submissions exist, deleting a question corrupts the data
            if (submissionService.getSubmissionCountOfSurvey(parentForm.getId()) > 0) {
                throw exceptionFactory.createCustomException("Question",
                        Collections.singletonList("submissionCount"),
                        Collections.singletonList(submissionService.getSubmissionCountOfSurvey(parentForm.getId())),
                        FormError.SUBMISSIONS_EXIST);
            }
            
            // update the updatedAt property
            surveyService.updateSurveyUpdatedAt(parentForm.getId());
        }

        // CascadeType.ALL handles options deletion now
        if (question.getOptions() != null) {
            question.getOptions().clear();
        }

        questionRepository.delete(question);
    }

    /**
     * Hard delete all questions for a specific form
     */
    @Transactional
    @Override
    public void batchDeleteQuestions(DeleteQuestionDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);

        List<UUID> questionIds = dto.getQuestionIdsList();
        List<Question> questions = questionRepository.findAllById(questionIds);

        // CascadeType.ALL handles options deletion now
        // Database delete
        questionRepository.deleteAllInBatch(questions);
    }

    private void updateParentSurveyUpdatedAt(Question question) {
        if (question.getGroupDimension() != null &&
            question.getGroupDimension().getDimension() != null &&
            question.getGroupDimension().getDimension().getSurveyForm() != null) {
            surveyService.updateSurveyUpdatedAt(question.getGroupDimension().getDimension().getSurveyForm().getId());
        }
    }

}
