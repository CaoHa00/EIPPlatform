package com.EIPplatform.model.dto.permitshistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePermitRequest {
    @NotBlank(message = "Permit type must not be blank")
    @Size(max = 255, message = "Permit type must not exceed 255 characters")
    String permitType;

    @Size(max = 100, message = "Permit number must not exceed 100 characters")
    String permitNumber;

    @NotBlank(message = "Project name must not be blank")
    @Size(max = 500, message = "Project name must not exceed 500 characters")
    String projectName;

    @NotNull(message = "Issue date must not be null")
    @PastOrPresent(message = "Issue date cannot be in the future")
    LocalDate issueDate;

    @NotBlank(message = "Issuing organization must not be blank")
    @Size(max = 500, message = "Issuing organization must not exceed 500 characters")
    String issuerOrg;
}
