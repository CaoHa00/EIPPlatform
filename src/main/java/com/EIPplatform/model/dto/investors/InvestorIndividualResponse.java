package com.EIPplatform.model.dto.investors;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import com.EIPplatform.model.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorIndividualResponse extends InvestorResponse {
    String name;
    Gender gender;
    LocalDate dateOfBirth;
    Boolean isNative;
    String identificationNumber;
    String passportId;
    String nationality;
}