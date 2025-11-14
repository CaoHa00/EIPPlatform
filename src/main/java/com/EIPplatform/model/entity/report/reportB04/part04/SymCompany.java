package com.EIPplatform.model.entity.report.reportB04.part04;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SymCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sc_id")
    Long scId;

    @Column(name = "sym_company_name", columnDefinition = "NVARCHAR(255)")
    String symCompanyName;

    //type = internal || external
    @Column(name = "sym_company_type", columnDefinition = "NVARCHAR(255)")
    String symCompanyType;

    // //============RELATIONSHIP===============
    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "si_id", nullable = false)
    // SymbiosisIndustry symbiosisIndustry;
}
