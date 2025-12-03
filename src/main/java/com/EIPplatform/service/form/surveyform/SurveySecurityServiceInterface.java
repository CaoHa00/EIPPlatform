package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.Question;
import com.EIPplatform.model.entity.form.surveyform.QuestionOption;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;

import java.util.UUID;

@Deprecated
public interface SurveySecurityServiceInterface {
    SurveyForm getFormIfCreator(UUID formId, UUID userAccountId);
    Question getQuestionIfCreator(UUID questionId, UUID userAccountId);
    QuestionOption getOptionIfCreator(UUID optionId, UUID userAccountId);
}
