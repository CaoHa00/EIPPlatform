package com.EIPplatform.model.entity.businessInformation.investors;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "investor_organization_detail")
@DiscriminatorValue("ORGANIZATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorOrganizationDetail extends Investor {

    @Column(name = "organization_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String organizationName;

    @Column(name = "organization_legaldoc_type", nullable = false, columnDefinition = "NVARCHAR(255)")
    String organizationLegalDocType;

    @Column(name = "organization_issue_date", nullable = false)
    LocalDate organizationIssueDate;

    @Column(name = "organization_issuer_org", nullable = false, columnDefinition = "NVARCHAR(255)")
    String organizationIssuerOrg;

    @Column(name = "organization_address", nullable = true, columnDefinition = "NVARCHAR(255)")
    String organizationAddress;

    @Column(name = "organization_website", nullable = true, columnDefinition = "NVARCHAR(255)")
    String organizationWebsite;

}
