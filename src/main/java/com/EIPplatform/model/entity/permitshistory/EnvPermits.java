package com.EIPplatform.model.entity.permitshistory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
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

    @Column(name = "permit_number", nullable = false)
            String permitNumber;

    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate;

    @Column(name = "issuer_org", nullable = false)
    String issuerOrg;

    @Column(name = "project_name")
    String projectName;

    @Column(name = "permit_file_path")
    String permitFilePath;

    @Column(name = "is_active", columnDefinition = "BIT DEFAULT 1")
    Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;
}