package com.EIPplatform.model.dto.report.reportB04.part1.response;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorResponse;
import com.EIPplatform.model.dto.legalDoc.LegalDocDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ThirdPartyImplementerDTO;

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
public class ReportInvestorDetailResponse {
    Long ridId;
    String reportInvestorType;
    InvestorResponse investor;
    LegalDocDTO legalDoc;
    ThirdPartyImplementerDTO thirdPartyImplementer;
}
