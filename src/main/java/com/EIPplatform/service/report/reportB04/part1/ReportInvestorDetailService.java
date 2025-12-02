package com.EIPplatform.service.report.reportB04.part1;

import java.util.UUID;

import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
public interface ReportInvestorDetailService {

    ReportInvestorDetailDTO createReportInvestorDetailDTO(UUID reportId, UUID businessDetailId, ReportInvestorDetailCreateRequest request);
    ReportInvestorDetailDTO getReportInvestorDetailDTO(UUID reportId, UUID businessDetailId);   
    //ReportInvestorDetailDTO getInitialReportInvestorDetailDTO (UUID reportId, UUID businessDetailId); // get all information that user already had.
    // void deleteReportInvestorDetailDTO(UUID reportId, UUID businessDetailId);
    // boolean hasAirEmissionDataFile(UUID reportId, UUID businessDetailId);
}