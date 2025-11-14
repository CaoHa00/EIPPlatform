package com.EIPplatform.model.entity.report.reportB04.part01;

import java.time.LocalDate;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Table(name = "third_party_implementer", indexes = {
        @Index(name = "idx_tpi_org_name", columnList = "org_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ThirdPartyImplementer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tpi_id", updatable = false, nullable = false)
    Long tpiId;

    @Nationalized
    @Column(name = "org_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String orgName;

    @Nationalized
    @Column(name = "org_doc_type", nullable = false, columnDefinition = "NVARCHAR(100)")
    String orgDocType;

    @Column(name = "org_doc_number", nullable = false, columnDefinition = "VARCHAR(100)")
    String orgDocNumber;

    @Nationalized
    @Column(name = "org_doc_issuer", nullable = false, columnDefinition = "NVARCHAR(255)")
    String orgDocIssuer;

    @Column(name = "org_doc_issue_date", nullable = false)
    LocalDate orgDocIssueDate;

    @Column(name = "org_doc_amend_date")
    LocalDate orgDocAmendDate;
}
