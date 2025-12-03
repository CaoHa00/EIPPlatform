package com.EIPplatform.repository.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findByGroupDimension_Dimension_SurveyForm_IdAndActiveTrue(UUID surveyId);
    Optional<Question> findByDisplayOrderAndActiveTrue(Integer displayOrder);
    boolean existsByGroupDimension_Dimension_Id(UUID id);
    boolean existsByGroupDimension_Id(UUID id);
}