package com.EIPplatform.model.entity.permitshistory;

import com.EIPplatform.model.entity.user.userInformation.BusinessDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "env_component_permits", indexes = {
        @Index(name = "idx_permit_number", columnList = "permit_number"),
        @Index(name = "idx_business_detail_id", columnList = "business_detail_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnvComponentPermit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permit_id", updatable = false, nullable = false)
    Long permitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_detail_id")
    @JsonBackReference(value = "businessDetail-componentPermits")
    BusinessDetail businessDetail;

    @Column(name = "permit_type", nullable = false)
    String permitType;

    @Column(name = "project_name", nullable = false)
    String projectName;

    @Column(name = "permit_number", nullable = false)
    String permitNumber;

    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate;

    @Column(name = "issuer_org", nullable = false)
    String issuerOrg;

    @Column(name = "permit_file_path")
    String permitFilePath;

    @Column(name = "is_active", columnDefinition = "BIT DEFAULT 1")
    Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;
}