package com.EIPplatform.repository.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface DimensionRepository extends JpaRepository<Dimension, UUID> {
    List<Dimension> findBySurveyFormId(UUID surveyFormId);
}
