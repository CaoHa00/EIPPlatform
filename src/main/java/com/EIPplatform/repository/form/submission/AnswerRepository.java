package com.EIPplatform.repository.form.submission;

import com.EIPplatform.model.entity.form.submission.Answer;
import com.EIPplatform.model.entity.form.submission.Submission;
import com.EIPplatform.model.entity.form.surveyform.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    boolean existsBySubmissionAndQuestion(Submission submission, Question question);
}
