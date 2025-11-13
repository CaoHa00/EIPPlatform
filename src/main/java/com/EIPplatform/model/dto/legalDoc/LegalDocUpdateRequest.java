package com.EIPplatform.model.dto.legalDoc;


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
public class LegalDocUpdateRequest {

    @NotNull(message = "FIELD_REQUIRED")
    UUID legalDocId;

    @NotBlank(message = "FIELD_REQUIRED")
    String type;

    @NotBlank(message = "FIELD_REQUIRED")
    String number;

    @NotBlank(message = "FIELD_REQUIRED")
    String issuerOrg;

    @NotNull(message = "FIELD_REQUIRED")
    LocalDate issueDate;

    Integer amendmentCount;

    LocalDate issueDateLatest;

    String projectName;

    @NotNull(message = "FIELD_REQUIRED")
    UUID investorOrganizationId;
}