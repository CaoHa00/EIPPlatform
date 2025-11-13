package com.EIPplatform.model.dto.legalRepresentative;

import java.time.LocalDate;
import java.util.UUID;

import com.EIPplatform.model.enums.Gender;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LegalRepresentativeDTO {
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
}
