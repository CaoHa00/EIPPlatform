package com.EIPplatform.service.form.submission;


import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.FormError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.exception.errorCategories.ValidationError;
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
import com.EIPplatform.repository.form.submission.SubmissionRepository;
import com.EIPplatform.repository.form.surveyform.*;

import com.EIPplatform.service.authentication.UserAccountImplementation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final SurveyFormRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final UserAccountImplementation userService;
    private final UserAccountRepository userAccountRepository;
    private final ExceptionFactory exceptionFactory;

    public SubmissionService(SubmissionRepository submissionRepository,
                             SurveyFormRepository surveyRepository,
                             QuestionRepository questionRepository,
                             UserAccountImplementation userService, UserAccountRepository userAccountRepository, ExceptionFactory exceptionFactory) {
        this.submissionRepository = submissionRepository;
        this.surveyRepository = surveyRepository;
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.userAccountRepository = userAccountRepository;
        this.exceptionFactory = exceptionFactory;
    }

    public SubmissionDTO getSubmissionById(UUID id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Submission", "id", id, FormError.SUBMISSION_NOT_FOUND));
        return mapToDTO(submission);
    }

    public List<SubmissionDTO> getSubmissionByRespondentId(UUID id) {
        UserAccount respondent = userAccountRepository.findById(id).orElseThrow(() -> exceptionFactory.createNotFoundException("User", "id", id, UserError.NOT_FOUND));
        List<Submission> submission = submissionRepository.findByRespondent(respondent);
        return  submission.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<SubmissionDTO> getSubmissionByFormId(UUID id) {
        List<Submission> submission = submissionRepository.findBySurveyFormId(id);
        return  submission.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<SubmissionDTO> getAllSubmissions() {
        List<Submission> submission = submissionRepository.findAll();
        return  submission.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public long getSubmissionCountOfSurvey(UUID surveyFormId){
        return submissionRepository.countBySurveyFormId(surveyFormId);
    }

    @Transactional
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
        submission.setSubmittedAt(LocalDateTime.now());

        // handle User
        try {
            UserAccount currentUser = userService.getCurrentUser();
            submission.setRespondent(currentUser);
        } catch (Exception e) {
            // catch anonymous submission
        }

        // process Answers
        if (dto.getAnswers() != null && !dto.getAnswers().isEmpty()) {
            List<Answer> answerEntities = processAnswers(dto.getAnswers(), form, submission);
            submission.setAnswers(answerEntities);
        }

        // save
        Submission saved = submissionRepository.save(submission);
        return mapToDTO(saved);
    }

    @Transactional
    public void softDeleteSubmission(UUID submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Submission", "id", submissionId, FormError.SUBMISSION_NOT_FOUND));

        submission.setDeleted(true);
        submissionRepository.save(submission);
    }

    @Transactional
    public void hardDeleteSubmission(UUID submissionId) {
        if (!submissionRepository.existsById(submissionId)) {
            throw exceptionFactory.createNotFoundException("Submission", "id", submissionId, FormError.SUBMISSION_NOT_FOUND);
        }
        // Submission -> Answer has CascadeType.ALL
        submissionRepository.deleteById(submissionId);
    }

    //helper
    private List<Answer> processAnswers(List<CreateAnswerDTO> answerDtos, SurveyForm form, Submission submission) {
        List<Answer> result = new ArrayList<>();

        Map<UUID, Question> questionMap = questionRepository.findBySurveyFormIdAndActiveTrue(form.getId())
                .stream().collect(Collectors.toMap(Question::getId, Function.identity()));

        for (CreateAnswerDTO ansDto : answerDtos) {
            Question question = questionMap.get(ansDto.getQuestionId());

            if (question == null) continue;

            String value = ansDto.getValue();

            if (question.isRequired() && (value == null || value.isBlank())) {
                throw exceptionFactory.createValidationException("Question", "text", question.getText(), ValidationError.REQUIRED_QUESTION);
            }

            if (value != null && !value.isBlank()) {
                Answer answer = new Answer();
                answer.setSubmission(submission);
                answer.setQuestion(question);

                // save the string provided by frontend
                answer.setValue(value);

                result.add(answer);
            }
        }

        return result;
    }

    private SubmissionDTO mapToDTO(Submission submission) {
        SubmissionDTO dto = new SubmissionDTO();
        dto.setId(submission.getId());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setSurveyFormTitle(submission.getSurveyForm().getTitle());
        if (submission.getRespondent() != null) {
            dto.setRespondentUsername(submission.getRespondent().getFullName());
        }

        // map Answers
        if (submission.getAnswers() != null) {
            List<AnswerDTO> answerDTOs = submission.getAnswers().stream().map(ans -> {
                AnswerDTO aDto = new AnswerDTO();
                aDto.setId(ans.getId());
                aDto.setQuestionId(ans.getQuestion().getId());
                aDto.setValue(ans.getValue());
                return aDto;
            }).collect(Collectors.toList());
            dto.setAnswers(answerDTOs);
        }
        return dto;
    }
}
