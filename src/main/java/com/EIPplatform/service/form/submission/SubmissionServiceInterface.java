package com.EIPplatform.service.form.submission;

import com.EIPplatform.model.dto.form.submission.CreateSubmissionDTO;
import com.EIPplatform.model.dto.form.submission.SubmissionDTO;

import java.util.List;
import java.util.UUID;

public interface SubmissionServiceInterface {
    SubmissionDTO getSubmissionById(UUID id);
    List<SubmissionDTO> getSubmissionByRespondentId(UUID id);
    List<SubmissionDTO> getSubmissionByFormId(UUID id);
    List<SubmissionDTO> getAllSubmissions();
    long getSubmissionCountOfSurvey(UUID surveyFormId);
    SubmissionDTO createSubmission(CreateSubmissionDTO dto);
    void softDeleteSubmission(UUID submissionId);
    void hardDeleteSubmission(UUID submissionId);
}
