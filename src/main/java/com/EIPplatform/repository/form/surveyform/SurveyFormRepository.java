package com.EIPplatform.repository.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.SurveyForm;
import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface SurveyFormRepository extends JpaRepository<SurveyForm, UUID>, JpaSpecificationExecutor<SurveyForm> {
    List<SurveyForm> findByCreator(UserAccount creator);

    List<SurveyForm> findBySurveyFormCategory(SurveyFormCategory category);

    boolean existsBySurveyFormCategory_CategoryId(UUID categoryId);
}
