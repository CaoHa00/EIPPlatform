package com.EIPplatform.service.report.reportB04.part1;

import java.util.UUID;

import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
public interface ReportInvestorDetailService {

    ReportInvestorDetailDTO createReportInvestorDetailDTO(UUID reportId, UUID userAccountId, ReportInvestorDetailCreateRequest request);
    ReportInvestorDetailDTO getReportInvestorDetailDTO(UUID reportId, UUID userAccountId);   
    ReportInvestorDetailDTO getInitialReportInvestorDetailDTO (UUID reportId, UUID userAccountId); // get all information that user already had.
    // void deleteReportInvestorDetailDTO(UUID reportId, UUID userAccountId);
    // boolean hasAirEmissionDataFile(UUID reportId, UUID userAccountId);
}