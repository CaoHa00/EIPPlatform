package com.EIPplatform.model.dto.businessInformation.permitshistory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateComponentPermitRequest {

    @NotBlank(message = "Permit type is required")
    private String permitType;

    @NotBlank(message = "Project name is required")
    private String projectName;

    @NotBlank(message = "Permit number is required")
    private String permitNumber;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotBlank(message = "Issuer organization is required")
    private String issuerOrg;
}