package com.EIPplatform.service.form.submission;


import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.exception.errorCategories.ValidationError;
import com.EIPplatform.mapper.form.submission.AnswerMapper;
import com.EIPplatform.mapper.form.submission.SubmissionMapper;
import com.EIPplatform.model.dto.form.submission.AnswerDTO;
import com.EIPplatform.model.dto.form.submission.CreateAnswerDTO;
import com.EIPplatform.model.dto.form.submission.CreateSubmissionDTO;
import com.EIPplatform.model.dto.form.submission.SubmissionDTO;
import com.EIPplatform.model.entity.form.submission.Answer;
import com.EIPplatform.model.entity.form.submission.Submission;
import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.form.submission.AnswerRepository;
import com.EIPplatform.repository.form.submission.SubmissionRepository;
import com.EIPplatform.repository.form.surveyform.*;

import com.EIPplatform.service.authentication.UserAccountImplementation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService implements SubmissionServiceInterface {
    private final SubmissionRepository submissionRepository;
    private final SurveyFormRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final UserAccountImplementation userService;
    private final UserAccountRepository userAccountRepository;
    private final AnswerRepository answerRepository;
    private final ExceptionFactory exceptionFactory;
    private final SubmissionMapper submissionMapper;
    private final AnswerMapper answerMapper;

    @Override
    public SubmissionDTO getSubmissionById(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Submission", "id", id, FormError.SUBMISSION_NOT_FOUND));
        return submissionMapper.toDTO(submission);
    }

    @Override
    public List<SubmissionDTO> getSubmissionByRespondentId(UUID id) {
        UserAccount respondent = userAccountRepository.findById(id).orElseThrow(() -> exceptionFactory.createNotFoundException("User", "id", id, UserError.NOT_FOUND));
        List<Submission> submission = submissionRepository.findByRespondent(respondent);
        return  submission.stream().map(submissionMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SubmissionDTO> getSubmissionByFormId(UUID id) {
        List<Submission> submission = submissionRepository.findBySurveyFormId(id);
        return  submission.stream().map(submissionMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SubmissionDTO> getAllSubmissions() {
        List<Submission> submission = submissionRepository.findAll();
        return  submission.stream().map(submissionMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public long getSubmissionCountOfSurvey(UUID surveyFormId){
        return submissionRepository.countBySurveyFormId(surveyFormId);
    }

    /**
     * Create a Submission with empty Answer list
     * Indicator Groups will submit their answers separately
     * @param dto CreateSubmissionDTO
     * @return SubmissionDTO
     */
    @Transactional
    @Override
    public SubmissionDTO createSubmission(CreateSubmissionDTO dto) {
        // fetch form
        SurveyForm form = surveyRepository.findById(dto.getFormId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException("SurveyForm", "id", dto.getFormId(), FormError.SURVEY_FORM_NOT_FOUND));

        // validate form status
        if (!form.isActive()) {
            throw exceptionFactory.createCustomException(ValidationError.FORM_INACTIVE);
        }
        if (form.getExpiresAt() != null && form.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw exceptionFactory.createCustomException(ValidationError.FORM_EXPIRED);
        }

        // create submission
        Submission submission = new Submission();
        submission.setSurveyForm(form);
        submission.setCreatedAt(LocalDateTime.now());
        submission.setUpdatedAt(LocalDateTime.now());

        // handle User
        try {
            //IMPLEMENT GET USER FROM REQUEST
//            UserAccount currentUser = userService.getCurrentUser();
//            submission.setRespondent(currentUser);
        } catch (Exception e) {
            // catch anonymous submission
        }

        // DEPRECATED process Answers.
        // Indicator group will submit answer separately
//        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
//            List<Answer> answerEntities = processAnswers(dto.getAnswers(), form, submission);
//            submission.setAnswers(answerEntities);
//        }

        // save
        Submission saved = submissionRepository.save(submission);
        return submissionMapper.toDTO(saved);
    }

    /**
     * Soft delete Submission toggle
     * Toggles the isDeleted boolean to true or false
     * @param submissionId submissionID
     */
    @Transactional
    @Override
    public void softDeleteSubmission(UUID submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Submission", "id", submissionId, FormError.SUBMISSION_NOT_FOUND));

        var newDeletedState = !submission.isDeleted();

        submission.setUpdatedAt(LocalDateTime.now());
        submission.setDeleted(newDeletedState);
        submissionRepository.save(submission);
    }

    /**
     * Hard delete, wipe all Submission and Answer entity from database
     * @param submissionId submissionId
     */
    @Transactional
    @Override
    public void hardDeleteSubmission(UUID submissionId) {
        if (!submissionRepository.existsById(submissionId)) {
            throw exceptionFactory.createNotFoundException("Submission", "id", submissionId, FormError.SUBMISSION_NOT_FOUND);
        }
        // Submission -> Answer has CascadeType.ALL
        submissionRepository.deleteById(submissionId);
    }

    @Transactional
    @Override
    public List<AnswerDTO> submitAnswers(List<@Valid CreateAnswerDTO> answerDTOList, UUID submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Submission", "id", submissionId, FormError.SUBMISSION_NOT_FOUND));

        // check if submission not deleted
        if (submission.isDeleted()){
            throw exceptionFactory.createCustomException(ValidationError.SUBMISSION_DELETED);
        }

        //update
        submission.setUpdatedAt(LocalDateTime.now());

        List<Answer> savedAnswers = new ArrayList<>();

        for (CreateAnswerDTO answerDTO : answerDTOList) {
            Question question = questionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("Question", "id", answerDTO.getQuestionId(), FormError.QUESTION_NOT_FOUND));

            // Check if an answer for this question already exists in the submission
            if (answerRepository.existsBySubmissionAndQuestion(submission, question)) {
                throw exceptionFactory.createCustomException(ValidationError.ANSWER_ALREADY_EXISTS);
            }

            Answer answer = new Answer();
            answer.setSubmission(submission);
            answer.setQuestion(question);
            answer.setValue(answerDTO.getValue());
            savedAnswers.add(answerRepository.save(answer));
        }

        return answerMapper.toDTOList(savedAnswers);
    }

    @Transactional
    @Override
    public AnswerDTO editAnswer(UUID answerID, String value){
        if (value == null){
            throw exceptionFactory.createCustomException(ValidationError.ANSWER_EMPTY_VALUE);
        }

        Answer answer = answerRepository.findById(answerID).orElseThrow(() ->
                exceptionFactory.createNotFoundException("Answer", "id", answerID, FormError.ANSWER_NOT_FOUND));

        Submission submission = answer.getSubmission();

        if (submission.isDeleted()){
            throw exceptionFactory.createCustomException(ValidationError.SUBMISSION_DELETED);
        }

        answer.setValue(value);
        submission.setUpdatedAt(LocalDateTime.now());
        return answerMapper.toDTO(answer);
    }
}
