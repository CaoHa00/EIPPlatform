package com.EIPplatform.model.entity.businessInformation.investors;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "investor", uniqueConstraints = @UniqueConstraint(columnNames = "tax_code"))
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "investor_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public abstract class Investor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "investor_id", columnDefinition = "uniqueidentifier")
    UUID investorId;

    @Column(name = "address", columnDefinition = "NVARCHAR(500)")
    String address;

    @Column(name = "tax_code", columnDefinition = "NVARCHAR(50)")
    String taxCode;

    @Column(name = "phone", columnDefinition = "NVARCHAR(20)", nullable = false)
    String phone;

    @Column(name = "fax", columnDefinition = "NVARCHAR(20)")
    String fax;

    @Column(name = "email", columnDefinition = "NVARCHAR(255)")
    String email;

    @Embedded
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