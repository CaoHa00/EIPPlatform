package com.EIPplatform.service.form.surveyform;

import java.util.UUID;

public interface SurveyAccessControlServiceInterface {
    void ensureBecamexRole(UUID userAccountId);
}
