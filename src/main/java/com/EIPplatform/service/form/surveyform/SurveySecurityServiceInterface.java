package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.QuestionOption;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;

import java.util.UUID;

public interface SurveySecurityServiceInterface {
    SurveyForm getFormIfCreator(UUID formId);
    Question getQuestionIfCreator(UUID questionId);
    QuestionOption getOptionIfCreator(UUID optionId);
}
