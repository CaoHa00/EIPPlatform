package com.EIPplatform.controller.report.reportB04.part4;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.reportB04.part4.SymbiosisIndustryDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.request.SymbiosisIndustryCreateRequestDTO;
import com.EIPplatform.service.report.reportB04.part4.SymbiosisIndustryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports-b04")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SymbiosisIndustryController {
    SymbiosisIndustryService symbiosisIndustryService;

    @PostMapping(value="/{reportId}/draft/part-4")
    public ApiResponse<SymbiosisIndustryDTO> createSymbiosisIndustry (
            @PathVariable UUID reportId,
            @RequestParam UUID businessDetailId,
            @RequestPart("data") @Valid SymbiosisIndustryCreateRequestDTO request
    ) {
        var result = symbiosisIndustryService.createReportB04Part4(reportId, businessDetailId, request);
        return ApiResponse.<SymbiosisIndustryDTO>builder()
                .result(result)
                .build();
    }
    @GetMapping(value="/{reportId}/draft/part-4")
    public ApiResponse<SymbiosisIndustryDTO> getSymbiosisIndustry (
            @PathVariable UUID reportId,
            @RequestParam UUID businessDetailId
    ) {
        var result = symbiosisIndustryService.getReportB04Part4(reportId, businessDetailId);
        return ApiResponse.<SymbiosisIndustryDTO>builder()
                .result(result)
                .build();
    }
    @DeleteMapping(value="/{reportId}/draft/part-4")
    public ApiResponse<Void> deleteReportB04Part4 (
            @PathVariable UUID reportId,
            @RequestParam UUID businessDetailId
    ) {
      symbiosisIndustryService.deleteReportB04Part4(reportId, businessDetailId);
      return ApiResponse.<Void>builder()
              .build();
    }
}
