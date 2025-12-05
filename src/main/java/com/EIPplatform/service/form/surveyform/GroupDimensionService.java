package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.model.entity.form.submission.SubmissionType;
import com.EIPplatform.mapper.form.submission.AnswerMapper;
import com.EIPplatform.mapper.form.surveyform.QuestionMapper;
import com.EIPplatform.model.dto.form.submission.AnswerDTO;
import com.EIPplatform.model.dto.form.surveyform.QuestionsAndAnswersDTO;
import com.EIPplatform.model.entity.form.submission.Answer;
import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.mapper.form.surveyform.GroupDimensionMapper;
import com.EIPplatform.model.dto.form.surveyform.question.CreateGroupDimensionDTO;
import com.EIPplatform.model.dto.form.surveyform.question.GroupDimensionDTO;
import com.EIPplatform.model.entity.form.surveyform.GroupDimension;
import com.EIPplatform.model.entity.form.surveyform.Dimension;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.form.submission.AnswerRepository;
import com.EIPplatform.repository.form.surveyform.GroupDimensionRepository;
import com.EIPplatform.repository.form.surveyform.QuestionRepository;
import com.EIPplatform.repository.form.surveyform.DimensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupDimensionService implements GroupDimensionServiceInterface {

    private final GroupDimensionRepository groupDimensionRepository;
    private final DimensionRepository dimensionRepository;
    private final GroupDimensionMapper groupDimensionMapper;
    private final ExceptionFactory exceptionFactory;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyAccessControlServiceInterface accessControlService;
    private final UserAccountRepository userAccountRepository;
    private final QuestionMapper questionMapper;
    private final AnswerMapper answerMapper;

    /**
     * Retrieves questions for a specific group dimension where the question's inputBusiness matches the BusinessDetail
     * of the authenticated user. It also returns the input answers provided by that user's business for these questions.
     *
     * @param groupDimensionId The ID of the group dimension.
     * @param userAccountId The ID of the authenticated user whose business detail is used to filter questions and answers.
     * @return QuestionsAndAnswersDTO containing relevant questions and input answers.
     */
    @Override
    public QuestionsAndAnswersDTO getQuestionsAndAnswersByGroupDimensionId(UUID groupDimensionId, UUID userAccountId) {
        if (!groupDimensionRepository.existsById(groupDimensionId)) {
            throw exceptionFactory.createNotFoundException("GroupDimension", "id", groupDimensionId, FormError.GROUP_DIMENSION_NOT_FOUND);
        }

        UserAccount currentUser = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("User", "id", userAccountId, UserError.NOT_FOUND));

        List<Question> questions = questionRepository.findAllByGroupDimensionIdAndInputBusiness(groupDimensionId, currentUser.getBusinessDetail());
        List<Answer> answers = answerRepository.findByQuestion_GroupDimension_IdAndSubmission_SubmissionTypeAndSubmission_BusinessDetail(groupDimensionId, SubmissionType.INPUT, currentUser.getBusinessDetail());
        QuestionsAndAnswersDTO dto = new QuestionsAndAnswersDTO();
        dto.setQuestions(questionMapper.toDTOList(questions));
        dto.setInputAnswers(answerMapper.toDTOList(answers));
        return dto;
    }

    /**
     * Retrieves questions for a specific group dimension where the question's comparisonBusiness
     * matches the BusinessDetail of the authenticated user (acting as a provider).
     * It returns these questions along with all input answers related to these questions (from any customer)
     * and the comparison answers provided by the authenticated user's business for these questions.
     *
     * @param groupDimensionId The ID of the group dimension.
     * @param userAccountId The ID of the authenticated user (provider) whose business detail is used to filter questions and comparison answers.
     * @return QuestionsAndAnswersDTO containing relevant questions, input answers from all customers, and comparison answers from the provider.
     */
    @Override
    public QuestionsAndAnswersDTO getComparison(UUID groupDimensionId, UUID userAccountId){
        if (!groupDimensionRepository.existsById(groupDimensionId)) {
            throw exceptionFactory.createNotFoundException("GroupDimension", "id", groupDimensionId, FormError.GROUP_DIMENSION_NOT_FOUND);
        }

        UserAccount currentUser = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("User", "id", userAccountId, UserError.NOT_FOUND));

        List<Question> questions = questionRepository.findAllByGroupDimensionIdAndComparisonBusiness(groupDimensionId, currentUser.getBusinessDetail());
        List<Answer> comparisonAnswers = answerRepository.findByQuestion_GroupDimension_IdAndSubmission_SubmissionTypeAndSubmission_BusinessDetail(groupDimensionId, SubmissionType.COMPARISON, currentUser.getBusinessDetail());
        List<Answer> inputAnswers = answerRepository.findByQuestionInAndSubmission_SubmissionType(questions, SubmissionType.INPUT);
        QuestionsAndAnswersDTO dto = new QuestionsAndAnswersDTO();
        dto.setQuestions(questionMapper.toDTOList(questions));
        dto.setComparisonAnswers(answerMapper.toDTOList(comparisonAnswers));
        dto.setInputAnswers(answerMapper.toDTOList(inputAnswers));

        return dto;
    }

    @Override
    public GroupDimensionDTO getById(UUID id) {
        GroupDimension gd = groupDimensionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("GroupDimension", "id", id, FormError.GROUP_DIMENSION_NOT_FOUND));
        return groupDimensionMapper.toDTO(gd);
    }

    @Override
    public List<GroupDimensionDTO> getAll() {
        return groupDimensionMapper.toDTOList(groupDimensionRepository.findAll());
    }

    @Override
    @Transactional
    public List<GroupDimensionDTO> createGroupDimensionList(List<CreateGroupDimensionDTO> createGroupDimensionDTOs, UUID dimensionId, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        Dimension parentDimension = dimensionRepository.findById(dimensionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Dimension", "id", dimensionId, FormError.DIMENSION_NOT_FOUND));

        List<GroupDimension> groupDimensions = createGroupDimensionDTOs.stream()
                .map(dto -> buildGroupDimensionEntity(dto, parentDimension))
                .collect(Collectors.toList());

        List<GroupDimension> savedGroupDimensions = groupDimensionRepository.saveAll(groupDimensions);

        return groupDimensionMapper.toDTOList(savedGroupDimensions);
    }

    @Override
    @Transactional
    public void deleteGroupDimension(UUID id, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        GroupDimension groupDimension = groupDimensionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("GroupDimension", "id", id, FormError.GROUP_DIMENSION_NOT_FOUND));

        //check if any existing Question entity uses this GroupDimension
        if (questionRepository.existsByGroupDimension_Id(id)) {
            throw exceptionFactory.createCustomException("GroupDimension",
                    Collections.singletonList("id"),
                    Collections.singletonList(id),
                    FormError.GROUP_DIMENSION_IN_USE);
        }

        groupDimensionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public GroupDimensionDTO editGroupDimension(UUID id, CreateGroupDimensionDTO dto, UUID userAccountId) {
        accessControlService.ensureBecamexRole(userAccountId);
        
        GroupDimension groupDimension = groupDimensionRepository.findById(id).orElseThrow(() ->
                exceptionFactory.createNotFoundException("GroupDimension", "id", id, FormError.GROUP_DIMENSION_NOT_FOUND));

        groupDimension.setName(dto.getName());
        groupDimension.setCode(dto.getCode());

        groupDimensionRepository.save(groupDimension);

        return groupDimensionMapper.toDTO(groupDimension);
    }

    public GroupDimension buildGroupDimensionEntity(CreateGroupDimensionDTO dto, Dimension parent) {
        GroupDimension groupDimension = new GroupDimension();
        groupDimension.setName(dto.getName());
        groupDimension.setDimension(parent);
        groupDimension.setCode(dto.getCode());
        return groupDimension;
    }
}
