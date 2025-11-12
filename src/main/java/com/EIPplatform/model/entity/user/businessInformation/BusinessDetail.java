package com.EIPplatform.model.entity.user.businessInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.enums.OperationType;
import jakarta.persistence.*;

import org.checkerframework.checker.units.qual.N;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.permitshistory.EnvComponentPermit;
import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.investors.Investor;
import com.EIPplatform.model.entity.user.legalRepresentative.LegalRepresentative;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "[business_detail]")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class BusinessDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "business_detail_id", columnDefinition = "uniqueidentifier")
    UUID businessDetailId;
    @Nationalized
    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    String facilityName;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_representative_id", referencedColumnName = "legal_representative_id", nullable = false, foreignKey = @ForeignKey(name = "fk_business_detail_legal_representative"))
    LegalRepresentative legalRepresentative;

    @Column(nullable = false, length = 20, columnDefinition = "NVARCHAR(255)")
    String phoneNumber;
    @Nationalized
    @Column(nullable = false, columnDefinition = "NVARCHAR(500)")
    String address;
    @Nationalized
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String activityType;
    @Nationalized
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String scaleCapacity;
    @Nationalized
    @Column(length = 100, columnDefinition = "NVARCHAR(255)")
    String ISO_certificate_14001;
    @Nationalized
    @Column(name = "iso_certificate_file_path", length = 255, columnDefinition = "NVARCHAR(255)")
    String isoCertificateFilePath;
    @Nationalized
    @Column(nullable = false, length = 50, columnDefinition = "NVARCHAR(255)")
    String businessRegistrationNumber;
    @Nationalized
    @Column(nullable = false, unique = true, length = 50, columnDefinition = "NVARCHAR(255)")
    String taxCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OperationType operationType = OperationType.REGULAR;
    @Nationalized
    @Column(length = 500, columnDefinition = "NVARCHAR(500)")
    String seasonalDescription;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id", referencedColumnName = "investor_id", foreignKey = @ForeignKey(name = "fk_business_detail_investor"))
    Investor investor;

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "businessDetail-historyConsumption-ref")
    @Builder.Default
    List<BusinessHistoryConsumption> businessHistoryConsumptions = new ArrayList<>();

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "businessDetail-account-ref")
    @Builder.Default
    List<UserAccount> userAccounts = new ArrayList<>();

    @Embedded
    @Builder.Default
    AuditMetaData auditMetaData = new AuditMetaData();

    @OneToMany(mappedBy = "businessDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "businessDetail-reports")
    List<ReportA05> reports;

    @OneToOne(mappedBy = "businessDetail", fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "businessDetail-mainPermit")
    EnvPermits envPermits;

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "businessDetail-componentPermits")
    List<EnvComponentPermit> envComponentPermits = new ArrayList<>();

    public LocalDateTime getCreatedAt() {
        return auditMetaData.getCreatedAt();
    }

    public String getCreatedBy() {
        return auditMetaData.getCreatedBy();
    }

    public LocalDateTime getUpdatedAt() {
        return auditMetaData.getUpdatedAt();
    }

    public String getUpdatedBy() {
        return auditMetaData.getUpdatedBy();
    }
}
