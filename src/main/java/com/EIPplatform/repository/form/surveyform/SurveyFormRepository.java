package com.EIPplatform.repository.form.surveyform;


import com.EIPplatform.model.entity.form.surveyform.SurveyForm;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SurveyFormRepository extends JpaRepository<SurveyForm, UUID> {

    // SELECT * FROM forms WHERE is_active = true
    List<SurveyForm> findByIsActiveTrue();

    List<SurveyForm> findByCreator(UserAccount creator);

    // find all active AND not expired
    // SELECT * FROM forms WHERE is_active = true AND (expires_at > ? OR expires_at IS NULL)
    List<SurveyForm> findByIsActiveTrueAndExpiresAtAfterOrExpiresAtIsNull(LocalDateTime now);

}
