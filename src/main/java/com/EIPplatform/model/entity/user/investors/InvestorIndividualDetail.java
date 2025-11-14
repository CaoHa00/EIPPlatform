package com.EIPplatform.model.entity.user.investors;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import com.EIPplatform.model.enums.Gender;
import com.EIPplatform.model.enums.InvestorType;

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

    @Column(name = "is_native", nullable = false)
    @Builder.Default
    Boolean isNative = false;

    @Column(name = "identification_number", columnDefinition = "NVARCHAR(50)", nullable = false)
    String identificationNumber;

    @Column(name = "passport_id", columnDefinition = "NVARCHAR(50)", nullable = false)
    String passportId;

    @Column(name = "nationality", columnDefinition = "NVARCHAR(100)", nullable = false)
    String nationality;

    @PrePersist
    public void prePersist() {
        this.investorType = InvestorType.INDIVIDUAL;
    }
}