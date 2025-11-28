package com.EIPplatform.repository.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {
    Optional<QuestionOption> findByDisplayOrderAndActiveTrue (int displayOrder);
}
