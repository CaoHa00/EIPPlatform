package com.EIPplatform.model.dto.form.surveyform.survey;

import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionCategoryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CreateSurveyFormCategoryDTO {
    @NotBlank(message = "Category name is required")
    private String name;

    @Valid
    private Set<CreateQuestionCategoryDTO> questionCategories = new HashSet<>();

}
