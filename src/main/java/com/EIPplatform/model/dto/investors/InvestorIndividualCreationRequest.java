package com.EIPplatform.model.dto.investors;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import com.EIPplatform.model.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorIndividualCreationRequest {

    @NotBlank(message = "FIELD_REQUIRED")
    String name;

    Gender gender;

    @NotNull(message = "FIELD_REQUIRED")
    @Past(message = "Date of birth must be in the past")
    LocalDate dateOfBirth;

    @NotNull(message = "FIELD_REQUIRED")
    Boolean isNative;

    @NotBlank(message = "FIELD_REQUIRED")
    String identificationNumber;

    @NotBlank(message = "FIELD_REQUIRED")
    String passportId;

    @NotBlank(message = "FIELD_REQUIRED")
    String nationality;

    String address;

    String taxCode;

    @NotBlank(message = "FIELD_REQUIRED")
    String phone;

    String fax;

    @Email(message = "Invalid email format")
    String email;
}