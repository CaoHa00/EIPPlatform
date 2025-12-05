package com.EIPplatform.model.entity.form.surveyform;

import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "forms_questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String code; //MS01, TR01

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(nullable = false)
    private String text;

    private boolean required = false;

    @Column(name = "is_active")
    private boolean active = true;

    private int displayOrder; //1 -> n

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comparison_business_id")
    private BusinessDetail comparisonBusiness;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_business_id")
    private BusinessDetail inputBusiness;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_dimension_id")
    private GroupDimension groupDimension;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options;
}
