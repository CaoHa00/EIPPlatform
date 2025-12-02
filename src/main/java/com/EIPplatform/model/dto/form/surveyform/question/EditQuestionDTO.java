package com.EIPplatform.model.dto.form.surveyform.question;

import lombok.Data;

import java.util.UUID;

@Data
public class EditQuestionDTO {
    private String text;
    private Boolean required;
    private UUID categoryId;
}
