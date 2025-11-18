package com.EIPplatform.model.entity.report.reportB04.part01;

import org.hibernate.annotations.Nationalized;

import com.EIPplatform.model.entity.businessInformation.Project;
import com.EIPplatform.model.entity.businessInformation.investors.Investor;
import com.EIPplatform.model.entity.legalDoc.LegalDoc;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(name = "report_investor_detail", indexes = {
        @Index(name = "idx_report_investor_type", columnList = "report_investor_type"),
        @Index(name = "idx_investor_id", columnList = "investor_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportInvestorDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid_id", updatable = false, nullable = false)
    Long ridId;

    @Nationalized
    @Column(name = "report_investor_type", nullable = false, columnDefinition = "NVARCHAR(100)")
    String reportInvestorType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_b04_id", nullable = false, unique = true)
    @JsonBackReference(value = "report-rid-ref")
    ReportB04 reportB04;

    /** 
     * Liên kết trực tiếp tới Investor (chính là investor_id)
     * Mỗi part chỉ có 1 investor → dùng @OneToOne
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "investor_id",
        referencedColumnName = "investor_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_report_investor_detail_investor")
    )
    Investor investor;

    /**
     * Các liên kết khác (ví dụ exec legal docs, third party, projects)
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_doc_id")
    LegalDoc legalDoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "third_party_implementer_id")
    ThirdPartyImplementer thirdPartyImplementer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    Project project;

}
