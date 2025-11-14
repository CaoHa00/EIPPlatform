package com.EIPplatform.model.entity.legalDoc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.entity.businessInformation.investors.InvestorOrganizationDetail;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "legal_docs")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class LegalDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "legal_doc_id", columnDefinition = "uniqueidentifier")
    UUID legalDocId;

    @Column(name = "type", nullable = false, columnDefinition = "NVARCHAR(100)")
    String type;

    @Column(name = "number", nullable = false, columnDefinition = "NVARCHAR(100)")
    String number;

    @Column(name = "issuer_org", nullable = false, columnDefinition = "NVARCHAR(255)")
    String issuerOrg;

    @Column(name = "issue_date", nullable = false)
    LocalDate issueDate;

    @Column(name = "amendment_count")
    Integer amendmentCount;

    @Column(name = "issue_date_latest")
    LocalDate issueDateLatest;

    @Column(name = "project_name", columnDefinition = "NVARCHAR(255)")
    String projectName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "investor_organization_id", referencedColumnName = "investor_id", foreignKey = @ForeignKey(name = "fk_legal_doc_investor_organization"))
    InvestorOrganizationDetail investorOrganization;

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
