package com.EIPplatform.model.dto.businessInformation.legalRepresentative;

import java.time.LocalDate;
import java.util.UUID;

import com.EIPplatform.model.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalRepresentativeCreationRequest {

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

    @NotNull(message = "FIELD_REQUIRED")
    String address;

    @NotNull(message = "FIELD_REQUIRED")
    String taxCode;

    @NotBlank(message = "FIELD_REQUIRED")
    String phone;

    String fax;

    @NotNull(message = "FIELD_REQUIRED")
    @Email(message = "Invalid email format")
    String email;

    @NotNull(message = "FIELD_REQUIRED")
    UUID BusinessDetailId;
}