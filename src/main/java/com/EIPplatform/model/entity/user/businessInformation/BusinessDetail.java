package com.EIPplatform.model.entity.user.businessInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.enums.OperationType;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.permitshistory.EnvComponentPermit;
import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
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

    @Column(nullable = false, unique = true)
    String companyName;
    
    @Column(nullable = false)
    String legalRepresentative;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false)
    String location;

    @Column(nullable = false)
    String industrySector;

    @Column(nullable = false)
    String scaleCapacity;

    @Column
    String ISO_certificate_14001;

    @Column(nullable = false)
    String businessRegistrationNumber;

    @Column(nullable = false, unique = true)
    String taxCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    OperationType operationType = OperationType.REGULAR;

    @Column(length = 500)
    String seasonalDescription;

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