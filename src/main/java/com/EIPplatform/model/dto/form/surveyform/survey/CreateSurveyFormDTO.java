package com.EIPplatform.model.dto.form.surveyform.survey;

import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateSurveyFormDTO {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message ="CategoryId is required")
    private UUID categoryId;

    @Valid
    private List<CreateQuestionDTO> questions;
}
