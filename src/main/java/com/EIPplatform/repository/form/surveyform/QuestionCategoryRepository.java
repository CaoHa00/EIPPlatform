package com.EIPplatform.repository.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, UUID> {
}
