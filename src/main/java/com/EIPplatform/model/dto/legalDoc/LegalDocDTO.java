package com.EIPplatform.model.dto.legalDoc;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalDocDTO {
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
}
