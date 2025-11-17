package com.EIPplatform.service.report.reportB04.part1;

import java.util.List;

import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ReportInvestorDetailResponse;

public interface ReportInvestorDetailService {
    ReportInvestorDetailResponse create(ReportInvestorDetailCreateRequest request);
    ReportInvestorDetailResponse update(Long id, ReportInvestorDetailUpdateRequest request);
    ReportInvestorDetailResponse getById(Long id);
    List<ReportInvestorDetailResponse> getAll();
    void delete(Long id);
}