package com.EIPplatform.model.entity.report.report06;

import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.report.ReportStatus;
import com.EIPplatform.model.entity.report.report06.part01.BusinessInformation;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import com.EIPplatform.model.entity.report.report06.part03.InventoryResultData;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Table(name = "report_06", indexes = {
        @Index(name = "idx_report06_year", columnList = "report_year"),
        @Index(name = "idx_report06_business_detail", columnList = "business_detail_id"),
        @Index(name = "idx_report06_submitted_by", columnList = "submitted_by"),
        @Index(name = "idx_report06_code", columnList = "report_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report06 {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "report_06_id", updatable = false, nullable = false)
    UUID report06Id;

    @Column(name = "report_name", unique = true, nullable = false)
    String reportName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_detail_id", nullable = false)
    @JsonBackReference(value = "businessDetail-report06")
    BusinessDetail businessDetail;

    @Column(name = "report_year", nullable = false)
    Integer reportYear;

    @Column(name = "reporting_period", length = 50)
    String reportingPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    @JsonBackReference(value = "account-submitted-06")
    UserAccount submittedBy;

    @Column(name = "submitted_at")
    LocalDateTime submittedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    @JsonBackReference(value = "account-reviewed-06")
    UserAccount reviewedBy;

    @Column(name = "reviewed_at")
    LocalDateTime reviewedAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "report_status_id", nullable = false)
//    ReportStatus reportStatus;

    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "part_1_06_id", nullable = false)
    @JsonManagedReference(value = "report06-business-info")
    BusinessInformation businessInformation; // Renamed back to BusinessInformation

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_2_06_id", nullable = false)
    @JsonManagedReference(value = "report06-operational-data")
    OperationalActivityData operationalActivityData; // Renamed back to OperationalActivityData

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_3_06_id", nullable = false)
    @JsonManagedReference(value = "report06-inventory-result")
    InventoryResultData inventoryResultData; // Renamed back to InventoryResultData

    @Column(name = "review_notes", columnDefinition = "NVARCHAR(MAX)")
    String reviewNotes;


    @Column(name = "version", nullable = false)
    Integer version;
    @Column(name = "is_deleted", columnDefinition = "BIT DEFAULT 0")
    Boolean isDeleted;


    @Column(name = "created_at", nullable = false, updatable = false,
            columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

}