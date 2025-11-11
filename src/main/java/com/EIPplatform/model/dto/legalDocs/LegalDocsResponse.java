package com.EIPplatform.model.dto.legalDocs;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

import com.EIPplatform.configuration.AuditMetaData;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalDocsResponse {

    UUID legalDocId;
    String type;
    String number;
    String issuerOrg;
    LocalDate issueDate;
    Integer amendmentCount;
    LocalDate issueDateLatest;
    String projectName;
    UUID investorOrganizationId;
    String investorOrganizationName;
    AuditMetaData auditMetaData;
}