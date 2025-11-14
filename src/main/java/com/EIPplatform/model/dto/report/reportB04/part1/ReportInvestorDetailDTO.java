package com.EIPplatform.model.dto.report.reportB04.part1;

import com.EIPplatform.model.dto.investors.InvestorResponse;
import com.EIPplatform.model.dto.legalDoc.LegalDocDTO;

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
public class ReportInvestorDetailDTO {
    Long ridId;
    String reportInvestorType;

    InvestorResponse investor;
    LegalDocDTO legalDoc;
    ThirdPartyImplementerDTO thirdPartyImplementer;

}
