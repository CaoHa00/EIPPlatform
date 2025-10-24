package com.EIPplatform.model.entity.user.userInformation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "[user_detail]")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uniqueidentifier")
    UUID userDetailId;

    @Column(nullable = false, unique = true)
    String companyName;

    @Column(nullable = false)
    String legalPresentative;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false)
    String location;

    @Column(nullable = false)
    String industrySector;

    @Column(nullable = false)
    String scaleCapacity;

    @Column(nullable = true)
    String ISO_certificate_14001;

    @Column(nullable = false)
    String businessRegistrationNumber;

    @Column(nullable = false, unique = true)
    String taxCode;

    @Column(nullable = true)
    String envPermitNumber;

    @Column(nullable = true)
    LocalDateTime envPermitIssueDate; // Ngày cấp phép môi trường

    @Column(nullable = true)
    String envPermitIssuer; // Cơ quan cấp phép môi trường

    @Column(nullable = true)
    String envPermitOthersNote;

    @OneToMany(mappedBy = "userDetail", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "userDetailsHistoryConsumption-ref")
    @Builder.Default
    List<UserHistoryConsumption> userHistoryConsumptions = new ArrayList<>();

    @OneToMany(mappedBy = "userDetail", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "userDetailsAccount-ref")
    @Builder.Default
    List<UserAccount> userAccounts = new ArrayList<>();

    @Embedded
    @Builder.Default
    AuditMetaData auditMetaData = new AuditMetaData();

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
