package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.dto.form.surveyform.survey.CreateSurveyFormCategoryDTO;
import com.EIPplatform.model.dto.form.surveyform.survey.SurveyFormCategoryDTO;

import java.util.List;

public interface SurveyFormCategoryServiceInterface {
    List<SurveyFormCategoryDTO>  createCategoryList(List<CreateSurveyFormCategoryDTO> list);
}
