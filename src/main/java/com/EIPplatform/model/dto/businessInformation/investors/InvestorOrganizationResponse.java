package com.EIPplatform.model.dto.businessInformation.investors;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorOrganizationResponse extends InvestorResponse {

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
}
