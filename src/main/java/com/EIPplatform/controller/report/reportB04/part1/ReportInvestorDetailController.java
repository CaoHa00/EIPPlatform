package com.EIPplatform.controller.report.reportB04.part1;

import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.service.report.reportB04.part1.ReportInvestorDetailService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/report-b04")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportInvestorDetailController {

    ReportInvestorDetailService service;

    @PostMapping(value = "/{reportId}/draft/part-1")
    public ApiResponse<ReportInvestorDetailDTO> createDraftPart1(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId,
            @RequestPart("data") @Valid ReportInvestorDetailCreateRequest request) {
        var result = service.createReportInvestorDetailDTO(reportId, userAccountId, request);
        return ApiResponse.<ReportInvestorDetailDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reportId}/draft/part-1")
    public ApiResponse<ReportInvestorDetailDTO> getDraftPart1(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        var result = service.getReportInvestorDetailDTO(reportId, userAccountId);
        return ApiResponse.<ReportInvestorDetailDTO>builder()
                .result(result)
                .build();
    }

     @GetMapping("/{reportId}/initial/part-1")
    public ApiResponse<ReportInvestorDetailDTO> getInitialDraftPart1(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        var result = service.getInitialReportInvestorDetailDTO(reportId,userAccountId);
        return ApiResponse.<ReportInvestorDetailDTO>builder()
                .result(result)
                .build();
    }

}
