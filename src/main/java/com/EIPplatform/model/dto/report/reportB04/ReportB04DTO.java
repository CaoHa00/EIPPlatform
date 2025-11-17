package com.EIPplatform.model.dto.report.reportB04;

import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.products.ProductDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportB04DTO {

    UUID reportId;
    String reportCode;
    UUID businessDetailId;
    String facilityName;
    Integer reportYear;
    String reportingPeriod;
    String reviewNotes;
    String inspectionRemedyReport;
    Double completionPercentage;
    Integer version;
    Boolean isDeleted;
    LocalDateTime createdAt;
    // ============= RELATIONSHIPS =============

    // -- part 1 --
    ReportInvestorDetailDTO reportInvestorDetail;

    // -- part 2 --
    ProductDTO product;

    // -- part 3 --
    // ResourcesSavingAndReductionDTO resourcesSavingAndReduction;

    // -- part 4 --
    // SymbiosisIndustryDTO symbiosisIndustry;
}
