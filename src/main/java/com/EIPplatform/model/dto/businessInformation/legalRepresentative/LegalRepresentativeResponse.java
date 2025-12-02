package com.EIPplatform.model.dto.businessInformation.legalRepresentative;

import java.time.LocalDate;
import java.util.UUID;

import com.EIPplatform.model.enums.Gender;

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
public class LegalRepresentativeResponse {

    UUID legalRepresentativeId;
    String name;
    Gender gender;
    Boolean isNativeResident;
    LocalDate dateOfBirth;
    String identificationNumber;
    String passportId;
    String nationality;
    String address;
    String taxCode;
    String phone;
    String fax;
    String email;
    // AuditMetaData auditMetaData;
}