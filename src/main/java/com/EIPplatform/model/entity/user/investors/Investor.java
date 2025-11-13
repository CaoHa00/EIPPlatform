package com.EIPplatform.model.entity.user.investors;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.enums.InvestorType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "investor")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "investor_type", insertable = false, updatable = false)
    InvestorType investorType;

    @Column(name = "address", columnDefinition = "NVARCHAR(500)")
    String address;

    @Column(name = "tax_code", columnDefinition = "NVARCHAR(50)", unique = true)
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