package com.EIPplatform.model.dto.form.submission;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class SubmissionDTO {
    private UUID id;
    private LocalDateTime submittedAt;
    private LocalDateTime updatedAt;
    private String surveyFormCategory;
    private UUID surveyFormId;
    private String surveyFormTitle;
    private UUID respondentId;
    private String respondentUsername;
    private List<AnswerDTO> answers;
}
