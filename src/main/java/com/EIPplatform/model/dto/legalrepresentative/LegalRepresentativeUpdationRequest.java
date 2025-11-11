package com.EIPplatform.model.dto.legalrepresentative;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

import com.EIPplatform.model.enums.Gender;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalRepresentativeUpdationRequest {

    @NotNull(message = "FIELD_REQUIRED")
    UUID legalRepresentativeId;

    @NotBlank(message = "FIELD_REQUIRED")
    String name;

    Gender gender;

    @NotNull(message = "FIELD_REQUIRED")
    Boolean isNativeResident;

    @NotNull(message = "FIELD_REQUIRED")
    @Past(message = "DATE_INVALID")
    LocalDate dateOfBirth;

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