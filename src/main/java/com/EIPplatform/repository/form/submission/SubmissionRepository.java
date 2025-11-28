package com.EIPplatform.repository.form.submission;


import com.EIPplatform.model.entity.form.submission.Submission;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
    long countBySurveyFormId(UUID surveyFormId);
    List<Submission> findBySurveyFormId(UUID surveyFormId);
    List<Submission> findBySurveyFormIdAndIsDeletedFalse(UUID surveyFormId);
    List<Submission> findByRespondent(UserAccount respondent);
}
