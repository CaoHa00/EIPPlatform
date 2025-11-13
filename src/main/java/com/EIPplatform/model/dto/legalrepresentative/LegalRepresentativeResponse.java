package com.EIPplatform.model.dto.legalRepresentative;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

import com.EIPplatform.configuration.AuditMetaData;
import com.EIPplatform.model.enums.Gender;

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
    AuditMetaData auditMetaData;
}