package com.EIPplatform.model.entity.form.submission;

import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.form.surveyform.GroupDimension;
import com.EIPplatform.model.entity.form.surveyform.SurveyForm;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "forms_submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private SurveyForm surveyForm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_dimension_id", nullable = false)
    private GroupDimension groupDimension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount respondent; //nullable, anonymous submission

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_detail_id")
    private BusinessDetail businessDetail;

    @Enumerated(EnumType.STRING)
    @Column(name = "submission_type")
    private SubmissionType submissionType;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
    private List<Answer> answers;
}
