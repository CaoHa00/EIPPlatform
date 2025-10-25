package com.EIPplatform.model.entity.report;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Entity
@Table(name = "report", indexes = {
        @Index(name = "idx_report_year_status", columnList = "report_year, status_id"),
        @Index(name = "idx_user_detail_id", columnList = "user_detail_id"),
        @Index(name = "idx_report_type_id", columnList = "report_type_id"),
        @Index(name = "idx_submitted_by", columnList = "submitted_by")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Report {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "report_id", updatable = false, nullable = false)
    UUID reportId;

    @Column(name = "report_code", unique = true)
    String reportCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_detail_id")
    @JsonBackReference(value = "userdetail-reports")
    UserDetail userDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_type_id")
    @JsonBackReference(value = "type-report")
    ReportType reportType;

    @Column(name = "report_year", nullable = false)
    Integer reportYear;

    @Column(name = "reporting_period")
    String reportingPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    @JsonBackReference(value = "status-report")
    ReportStatus reportStatus;

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

    @Column(name = "completion_percentage", precision = 5, scale = 2)
    BigDecimal completionPercentage;

    @Column(name = "version", nullable = false)
    Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_report_id")
    @JsonBackReference(value = "report-self")
    Report parentReport;

    @Column(name = "is_deleted", columnDefinition = "BIT DEFAULT 0")
    Boolean isDeleted;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "report-fields")
    List<ReportFields> reportFields;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "report-exceed")
    List<MonitorExceedance> monitorExceedances;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "report-waste")
    List<WasteStat> wasteStats;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "report-hazard")
    List<HazardWaste> hazardWastes;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "report-auto")
    List<AutoMonStat> autoMonStats;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "report-files")
    List<ReportFile> reportFiles;

    @OneToMany(mappedBy = "parentReport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference(value = "report-self")
    List<Report> childReports;
}
