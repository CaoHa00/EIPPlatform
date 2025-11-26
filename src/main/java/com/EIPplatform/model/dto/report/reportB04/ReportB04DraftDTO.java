package com.EIPplatform.model.dto.report.reportB04;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import com.EIPplatform.model.dto.businessInformation.products.ProductListDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.SymbiosisIndustryDTO;
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
public class ReportB04DraftDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    UUID reportId;
    String reportCode;
    UUID businessDetailId;
    Integer reportYear;
    String reportingPeriod;
    String facilityName;
    String reviewNotes;
    String inspectionRemedyReport;
    Integer version;
    Boolean isDeleted;
    LocalDateTime createdAt;
    // Tracking
    Double completionPercentage;

    // part1
    ReportInvestorDetailDTO reportInvestorDetail;

    // part2
    ProductListDTO products;

    //part3
    ResourcesSavingAndReductionDTO resourcesSavingAndReduction;

    //part4
    SymbiosisIndustryDTO symbiosisIndustry;

    // Metadata
    LocalDateTime lastModified;
    Integer currentStep;
    Boolean isDraft;

}
