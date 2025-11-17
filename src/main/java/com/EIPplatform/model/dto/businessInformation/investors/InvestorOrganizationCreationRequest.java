package com.EIPplatform.model.dto.businessInformation.investors;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    String address;

    String taxCode;

    @NotBlank(message = "FIELD_REQUIRED")
    String phone;

    String fax;

    @Email(message = "EMAIL_FORMAT")
    String email;
}