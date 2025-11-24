package com.EIPplatform.model.entity.report.reportB04;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import org.hibernate.annotations.UuidGenerator;

import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.products.Product;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import com.EIPplatform.model.entity.report.reportB04.part03.ResourcesSavingAndReduction;
import com.EIPplatform.model.entity.report.reportB04.part04.SymbiosisIndustry;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportB04 {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "report_id", updatable = false, nullable = false)
    UUID reportId;

    @Column(name = "report_code", unique = true, nullable = false, columnDefinition = "NVARCHAR(255)")
    String reportCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_detail_id", nullable = true)
    @JsonBackReference(value = "businessDetail-reports-B04")
    BusinessDetail businessDetail;

    @Column(name = "report_year", nullable = false)
    Integer reportYear;

    @Column(name = "reporting_period", length = 50, columnDefinition = "NVARCHAR(255)")
    String reportingPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    @JsonBackReference(value = "account-submitted-reportB04")
    UserAccount submittedBy;

    @Column(name = "submitted_at")
    LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    @JsonBackReference(value = "account-reviewed-reportB04")
    UserAccount reviewedBy;

    @Column(name = "reviewed_at")
    LocalDateTime reviewedAt;

    @Column(name = "review_notes", columnDefinition = "NVARCHAR(MAX)")
    String reviewNotes;
    
    @Column(name = "inspection_remedy_report", columnDefinition = "NVARCHAR(MAX)")
    String inspectionRemedyReport;

    @Column(name = "completion_percentage", precision = 5)
    Double completionPercentage;

    @Column(name = "version", nullable = false)
    Integer version;

    @Column(name = "is_deleted", columnDefinition = "BIT DEFAULT 0")
    Boolean isDeleted;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;
    // ============= RELATIONSHIPS =============

    // -- part 1 --
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference(value = "report-rid-ref")
    @JoinColumn(name = "rid_id",nullable = true)
    ReportInvestorDetail reportInvestorDetail;

    // -- part 2 --
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Product> products;
    
    // -- part 3 --
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "rsar_id",nullable = true)
    ResourcesSavingAndReduction resourcesSavingAndReduction;

    // -- part 4 --
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "si_id",nullable = true)
    SymbiosisIndustry symbiosisIndustry;

     @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (version == null) {
            version = 1;
        }
        if (isDeleted == null) {
            isDeleted = false;
        }
        if (completionPercentage == null) {
            completionPercentage = 0.0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
