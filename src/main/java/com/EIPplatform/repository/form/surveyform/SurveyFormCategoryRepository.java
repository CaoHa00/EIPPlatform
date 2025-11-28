package com.EIPplatform.repository.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.SurveyFormCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SurveyFormCategoryRepository extends JpaRepository<SurveyFormCategory, UUID> {
}
