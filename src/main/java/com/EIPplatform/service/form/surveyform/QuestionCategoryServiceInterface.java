package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;

public interface QuestionCategoryServiceInterface {
    QuestionCategory buildQuestionCategoryEntity(CreateQuestionCategoryDTO dto, SurveyFormCategory parent);
}
