package com.EIPplatform.model.entity.user.userInformation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.entity.permitsHistory.EnvPermits;
import com.EIPplatform.model.entity.permitsHistory.HistoryConsumption;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.EIPplatform.configuration.AuditMetaData;

import lombok.Builder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Builder
@Entity
@Table(name = "user_detail", indexes = {
        @Index(name = "idx_tax_code", columnList = "tax_code", unique = true),
        @Index(name = "idx_user_account_id", columnList = "user_account_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetail {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "user_detail_id", updatable = false, nullable = false)
    UUID userDetailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id", unique = true)
    @JsonBackReference(value = "useraccount-detail")
    UserAccount userAccount;

    @Column(name = "company_name", nullable = false)
    String companyName;

    @Column(name = "legal_representative", nullable = false)
    String legalRepresentative;

    @Column(name = "phone_number", nullable = false)
    String phoneNumber;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "industry_sector")
    String industrySector;

    @Column(name = "scale_capacity", columnDefinition = "NVARCHAR(MAX)")
    String scaleCapacity;

    @Column(name = "business_license_no", nullable = false)
    String businessLicenseNo;

    @Column(name = "tax_code", nullable = false, unique = true)
    String taxCode;

    @Column(name = "iso_14001_cert")
    String iso14001Cert;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME2 DEFAULT GETDATE()")
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "userDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "userdetail-permits")
    List<EnvPermits> envPermits;

    @OneToMany(mappedBy = "userDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "userdetail-history")
    List<HistoryConsumption> historyConsumptions;

    @OneToMany(mappedBy = "userDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "userdetail-reports")
    List<Report> reports;
}
