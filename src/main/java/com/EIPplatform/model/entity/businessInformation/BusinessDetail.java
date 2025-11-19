package com.EIPplatform.model.entity.businessInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.entity.businessInformation.investors.Investor;
import com.EIPplatform.model.entity.businessInformation.legalRepresentative.LegalRepresentative;
import com.EIPplatform.model.entity.businessInformation.permitshistory.EnvComponentPermit;
import com.EIPplatform.model.entity.businessInformation.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.businessInformation.products.Product;
import com.EIPplatform.model.enums.OperationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import org.hibernate.annotations.Nationalized;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
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
    
    @Column(nullable = false, length = 20, columnDefinition = "NVARCHAR(255)")
    String phoneNumber;

    @Column(nullable = false, columnDefinition = "NVARCHAR(500)")
    String address;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    String activityType;

    @Column(length = 100, columnDefinition = "NVARCHAR(255)")
    String ISO_certificate_14001;

    @Column(name = "iso_certificate_file_path", length = 255, columnDefinition = "NVARCHAR(255)")
    String isoCertificateFilePath;

    @Column(nullable = false, length = 50, columnDefinition = "NVARCHAR(255)")
    String businessRegistrationNumber;

    @Column(nullable = false, unique = true, length = 50)
    String taxCode;

    @Column(nullable = true, length = 50)
    String fax;

    @Nationalized
    @Email
    @Column(nullable = true, columnDefinition = "NVARCHAR(255)")
    String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    OperationType operationType = OperationType.REGULAR;

    @Column(length = 500, columnDefinition = "NVARCHAR(500)")
    String seasonalDescription;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "legal_representative_id")
    LegalRepresentative legalRepresentative;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_id")
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
    @JsonManagedReference(value = "businessDetail-mainPermit")
    EnvPermits envPermits;

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference(value = "businessDetail-componentPermits")
    List<EnvComponentPermit> envComponentPermits = new ArrayList<>();

    @OneToMany(mappedBy = "businessDetail", fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "businessDetail-projects")
    @Builder.Default
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "businessDetail", fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "businessDetail-facilities")
    @Builder.Default
    private List<Facility> facilities = new ArrayList<>();

    @OneToOne(mappedBy = "businessDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ScaleCapacity scaleCapacity;

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "businessDetail-processes")
    @Builder.Default
    private List<Process> processes = new ArrayList<>();

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "businessDetail-equipments")
    @Builder.Default
    private List<Equipment> equipments = new ArrayList<>();

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "businessDetail-products")
    @Builder.Default
    List<Product> products = new ArrayList<>();

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
