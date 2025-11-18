package com.EIPplatform.model.dto.businessInformation.investors;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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

    @NotNull(message = "FIELD_REQUIRED")
    UUID businessDetailId;
}