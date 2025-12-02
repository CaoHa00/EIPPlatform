package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.question.CreateQuestionCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.question.QuestionCategoryDTO;
import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;

import java.util.List;
import java.util.UUID;

public interface QuestionCategoryServiceInterface {
    QuestionCategory buildQuestionCategoryEntity(CreateQuestionCategoryDTO dto, SurveyFormCategory parent);
    QuestionCategoryDTO getById(UUID id);
    List<QuestionCategoryDTO> getAll();
    List<QuestionCategoryDTO> createCategoryList(List<CreateQuestionCategoryDTO> list, UUID surveyFormCategoryId);
    void deleteCategory(UUID categoryId);
    QuestionCategoryDTO editCategory(UUID categoryId, CreateQuestionCategoryDTO dto);
}
