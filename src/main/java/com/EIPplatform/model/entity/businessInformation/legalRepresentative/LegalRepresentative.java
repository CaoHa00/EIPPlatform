package com.EIPplatform.model.entity.businessInformation.legalRepresentative;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "is_native", nullable = true)
    @Builder.Default
    Boolean isNativeResident = false;

    @Column(name = "date_of_birth", nullable = true )
    LocalDate dateOfBirth;

    @Column(name = "identification_number", columnDefinition = "NVARCHAR(50)", nullable = true)
    String identificationNumber;

    @Column(name = "passport_id", columnDefinition = "NVARCHAR(50)", nullable = true)
    String passportId;

    @Column(name = "nationality", columnDefinition = "NVARCHAR(100)", nullable = true)
    String nationality;

    @Column(name = "address", columnDefinition = "NVARCHAR(500)")
    String address;

    @Column(name = "tax_code", columnDefinition = "VARCHAR(50)", unique = true)
    String taxCode;

    @Column(name = "phone", columnDefinition = "NVARCHAR(20)", nullable = true)
    String phone;

    @Column(name = "fax", columnDefinition = "NVARCHAR(20)")
    String fax;

    @Column(name = "email", columnDefinition = "NVARCHAR(255)")
    String email;

    @OneToOne(mappedBy = "legalRepresentative")
    BusinessDetail businessDetail;
    
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
