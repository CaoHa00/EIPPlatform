package com.EIPplatform.service.form.submission;

import com.EIPplatform.model.dto.form.submission.AnswerDTO;
import com.EIPplatform.model.dto.form.submission.CreateAnswerDTO;
import com.EIPplatform.model.dto.form.submission.CreateSubmissionDTO;
import com.EIPplatform.model.dto.form.submission.EditAnswerRequestDTO;
import com.EIPplatform.model.dto.form.submission.SubmissionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public interface SubmissionServiceInterface {
    SubmissionDTO getSubmissionById(UUID id);
    List<SubmissionDTO> getSubmissionByRespondentId(UUID id);
    List<SubmissionDTO> getSubmissionByFormId(UUID id);
    List<SubmissionDTO> getAllSubmissions();
    long getSubmissionCountOfSurvey(UUID surveyFormId);
    SubmissionDTO createSubmission(CreateSubmissionDTO dto, UUID userAccountId);
    void softDeleteSubmission(UUID submissionId, UUID userAccountId);
    void hardDeleteSubmission(UUID submissionId, UUID userAccountId);

    List<AnswerDTO> submitAnswers(@NotEmpty(message = "Please input at least ONE answer.") List<@Valid CreateAnswerDTO> answerDTOList, UUID submissionId, UUID userAccountId);
    AnswerDTO editAnswer(UUID answerID, String value, UUID userAccountId);
    List<AnswerDTO> batchEditAnswers(List<EditAnswerRequestDTO> requests, UUID userAccountId);
}
