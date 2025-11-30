package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.CreateSurveyFormCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyFormCategoryDTO;

import java.util.List;
import java.util.UUID;

public interface SurveyFormCategoryServiceInterface {
    SurveyFormCategoryDTO getById(UUID id);
    List<SurveyFormCategoryDTO> getAll();
    List<SurveyFormCategoryDTO>  createCategoryList(List<CreateSurveyFormCategoryDTO> list);
    void deleteCategory(UUID categoryId);

    SurveyFormCategoryDTO editCategoryName(UUID categoryId, String name);
}
