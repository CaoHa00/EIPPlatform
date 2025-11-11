package com.EIPplatform.model.dto.legalDocs;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalDocsDTO {
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
