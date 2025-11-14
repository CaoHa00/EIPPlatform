package com.EIPplatform.model.entity.user.investors;
import java.time.LocalDate;

import com.EIPplatform.model.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "investor_individual_detail")
@DiscriminatorValue("INDIVIDUAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorIndividualDetail extends Investor {

    @Column(name = "name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "NVARCHAR(20)")
    Gender gender;

    @Column(name = "date_of_birth", nullable = false)
    LocalDate dateOfBirth;

    @Column(name = "is_native", nullable = false, columnDefinition = "bit default 0")
    boolean isNative;

    @Column(name = "identification_number", columnDefinition = "NVARCHAR(50)", nullable = false)
    String identificationNumber;

    @Column(name = "passport_id", columnDefinition = "NVARCHAR(50)")
    String passportId;

    @Column(name = "nationality", columnDefinition = "NVARCHAR(100)", nullable = false)
    String nationality;
}