package com.EIPplatform.model.dto.report.reportB04.part1.request;

import java.util.UUID;
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
public class ReportInvestorDetailUpdateRequest {
    
    String reportInvestorType;

    // Sửa từ InvestorResponse -> UUID investorId
    UUID investorId;

    // Sửa từ LegalDocDTO -> Long legalDocId
    UUID legalDocId;

    // Sửa từ ThirdPartyImplementerDTO -> Long thirdPartyImplementerId
    Long thirdPartyImplementerId;
}