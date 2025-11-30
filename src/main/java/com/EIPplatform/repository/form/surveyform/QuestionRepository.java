package com.EIPplatform.repository.form.surveyform;

import com.EIPplatform.model.entity.form.surveyform.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, UUID> {
    List<Question> findBySurveyFormIdAndActiveTrue(UUID surveyId);
    Optional<Question> findByDisplayOrderAndActiveTrue(Integer displayOrder);

    boolean existsByQuestionCategory_SurveyFormCategory_CategoryId(UUID categoryId);
}
