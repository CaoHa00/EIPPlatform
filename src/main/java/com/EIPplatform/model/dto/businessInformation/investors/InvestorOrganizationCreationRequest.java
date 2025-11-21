package com.EIPplatform.model.dto.businessInformation.investors;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorOrganizationCreationRequest {

    @NotBlank(message = "FIELD_REQUIRED")
    String organizationName;

    @NotBlank(message = "FIELD_REQUIRED")
    String organizationLegalDocType;

    @NotNull(message = "FIELD_REQUIRED")
    LocalDate organizationIssueDate;

    @NotBlank(message = "FIELD_REQUIRED")
    String organizationIssuerOrg;

    String organizationAddress;
    
    String organizationWebsite;


    @NotBlank(message = "FIELD_REQUIRED")
    String taxCode;

    @NotBlank(message = "FIELD_REQUIRED")
    String phone;

    String fax;

    @Email(message = "Invalid email format")
    String email;

    String address;

    @NotNull(message = "FIELD_REQUIRED")
    UUID businessDetailId;
}
