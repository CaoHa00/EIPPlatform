package com.EIPplatform.model.entity.permitshistory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Nationalized;

import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(name = "env_permits", indexes = {
        @Index(name = "idx_permit_number", columnList = "permit_number"),
        @Index(name = "idx_business_detail_id", columnList = "business_detail_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnvPermits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permit_id", updatable = false, nullable = false)
    Long permitId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_detail_id", unique = true)
    @JsonBackReference(value = "businessDetail-mainPermit")
    BusinessDetail businessDetail;
    @Nationalized
    @Column(name = "permit_number", nullable = false, columnDefinition = "VARCHAR(255)")
    String permitNumber;

    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate;
    @Nationalized
    @Column(name = "issuer_org", nullable = false, columnDefinition = "NVARCHAR(255)")
    String issuerOrg;
    @Nationalized
    @Column(name = "project_name", columnDefinition = "NVARCHAR(255)")
    String projectName;
    @Nationalized
    @Column(name = "permit_file_path", columnDefinition = "VARCHAR(500)")
    String permitFilePath;

    @Column(name = "is_active", columnDefinition = "BIT DEFAULT 1")
    Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;
}
