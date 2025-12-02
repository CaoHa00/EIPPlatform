package com.EIPplatform.model.dto.form.surveyform.survey;

import com.EIPplatform.model.dto.form.surveyform.question.QuestionCategoryDTO;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class SurveyFormCategoryDTO {
    private UUID categoryId;
    private String name;
    private Set<QuestionCategoryDTO> questionCategories;
}
