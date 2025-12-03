package com.EIPplatform.model.dto.form.surveyform.question;

import lombok.Data;

import java.util.UUID;

@Data
public class EditQuestionDTO {
    private String text;
    private String code;
    private Boolean required;
    private UUID groupDimensionId;
}
