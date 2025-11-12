package com.EIPplatform.model.entity.report.report05;

import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UuidGenerator;

import java.lang.Double;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Table(name = "reporta05", indexes = {
        @Index(name = "idx_report_year", columnList = "report_year"),
        @Index(name = "idx_business_detail_id", columnList = "business_detail_id"),
        @Index(name = "idx_submitted_by", columnList = "submitted_by"),
        @Index(name = "idx_report_code", columnList = "report_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportA05 {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "report_id", updatable = false, nullable = false)
    UUID reportId;

    @Column(name = "report_code", unique = true, nullable = false)
    String reportCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_detail_id", nullable = true)
    @JsonBackReference(value = "businessDetail-reports")
    BusinessDetail businessDetail;

    @Column(name = "report_year", nullable = false)
    Integer reportYear;

    @Column(name = "reporting_period", length = 50)
    String reportingPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    @JsonBackReference(value = "account-submitted")
    UserAccount submittedBy;

    @Column(name = "submitted_at")
    LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    @JsonBackReference(value = "account-reviewed")
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

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference(value = "report-wastewater")
    WasteWaterData wasteWaterData;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference(value = "report-waste-management")
    WasteManagementData wasteManagementData;

    @OneToOne(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference(value = "report-air-emission")
    AirEmissionData airEmissionData;

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