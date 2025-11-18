package com.EIPplatform.model.entity.businessInformation.legalRepresentative;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "legal_representative")
@EntityListeners(AuditingEntityListener.class)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalRepresentative {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "legal_representative_id", columnDefinition = "uniqueidentifier")
    UUID legalRepresentativeId;

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "NVARCHAR(20)")
    Gender gender;

    @Column(name = "is_native", nullable = false)
    @Builder.Default
    Boolean isNativeResident = false;

    @Column(name = "date_of_birth", nullable = false)
    LocalDate dateOfBirth;

    @Column(name = "identification_number", columnDefinition = "NVARCHAR(50)", nullable = false)
    String identificationNumber;

    @Column(name = "passport_id", columnDefinition = "NVARCHAR(50)", nullable = false)
    String passportId;

    @Column(name = "nationality", columnDefinition = "NVARCHAR(100)", nullable = false)
    String nationality;

    @Column(name = "address", columnDefinition = "NVARCHAR(500)")
    String address;

    @Column(name = "tax_code", unique = true)
    String taxCode;

    @Column(name = "phone", columnDefinition = "NVARCHAR(20)", nullable = false)
    String phone;

    @Column(name = "fax", columnDefinition = "NVARCHAR(20)")
    String fax;

    @Column(name = "email", columnDefinition = "NVARCHAR(255)")
    String email;

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
