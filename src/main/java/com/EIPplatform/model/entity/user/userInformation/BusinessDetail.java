package com.EIPplatform.model.entity.user.userInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.entity.permitshistory.EnvPermits;
import com.EIPplatform.model.entity.report.Report_A05;
import jakarta.persistence.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "[bussiness_detail]")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class BusinessDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bussiness_detail_id", columnDefinition = "uniqueidentifier")
    UUID bussinessDetailId;

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
    String isoCertificate14001;

    @Column(nullable = false)
    String businessRegistrationNumber;

    @Column(nullable = false, unique = true)
    String taxCode;

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "bussinessDetail-historyConsumption-ref")
    @Builder.Default
    List<UserHistoryConsumption> userHistoryConsumptions = new ArrayList<>();

    @OneToMany(mappedBy = "businessDetail", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "businessDetail-account-ref")
    @Builder.Default
    List<UserAccount> userAccounts = new ArrayList<>();

    @Embedded
    @Builder.Default
    AuditMetaData auditMetaData = new AuditMetaData();

    @OneToMany(mappedBy = "businessDetail", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "businessDetail-reports")
    List<Report_A05> reports_A05;

    @OneToMany(mappedBy = "businessDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "bussinessDetail-permits")
    List<EnvPermits> envPermits;

}