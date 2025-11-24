package com.EIPplatform.model.dto.report.reportB04;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.products.ProductCreationListRequest;
import com.EIPplatform.model.dto.businessInformation.products.ProductDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
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
public class ReportB04DraftDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    UUID reportId;
    UUID businessDetailId;
    Integer reportYear;
    String reportingPeriod;

    // part1
    ReportInvestorDetailDTO reportInvestorDetailDTO;

    // part2
    ProductCreationListRequest productDTOs;

    //part3
    ResourcesSavingAndReductionDTO resourcesSavingAndReductionDTO;

    //part4
    SymbiosisIndustryDTO symbiosisIndustryDTO;

    // Metadata
    LocalDateTime lastModified;
    Integer currentStep;
    Boolean isDraft;

    // Tracking
    Integer completionPercentage;
}
