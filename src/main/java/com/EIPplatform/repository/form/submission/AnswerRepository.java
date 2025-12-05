package com.EIPplatform.repository.form.submission;

import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.form.submission.Answer;
import com.EIPplatform.model.entity.form.submission.Submission;
import com.EIPplatform.model.entity.form.submission.SubmissionType;
import com.EIPplatform.model.entity.form.surveyform.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    boolean existsBySubmissionAndQuestion(Submission submission, Question question);
    List<Answer> findByQuestion_GroupDimension_Id(UUID groupDimensionId);
    List<Answer> findByQuestion_GroupDimension_IdAndSubmission_SubmissionTypeAndSubmission_BusinessDetail(UUID question_groupDimension_id, SubmissionType submission_submissionType, BusinessDetail submission_businessDetail);
    List<Answer> findByQuestionInAndSubmission_SubmissionType(List<Question> questions, SubmissionType submissionType);
}
