package com.EIPplatform.model.dto.form.surveyform.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateQuestionCategoryDTO {
    @NotBlank(message = "Category name is required")
    private String name;

    @NotBlank(message = "Category code is required. E.G: Indicator Group 1")
    private String code;
}
