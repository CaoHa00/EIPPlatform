package com.EIPplatform.controller.report;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.ReportStatusDto;
import com.EIPplatform.service.report.ReportStatusInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/report-status")
@RequiredArgsConstructor
public class ReportStatusController {
     private final ReportStatusInterface reportStatusService;

    @PostMapping
    ApiResponse<ReportStatusDto> createReportStatus(@RequestBody @Valid ReportStatusDto dto) {
        var result = reportStatusService.create(dto);
        return ApiResponse.<ReportStatusDto>builder()
                .result(result)
                .message("Report status created successfully")
                .build();
    }

    @GetMapping
    ApiResponse<List<ReportStatusDto>> getAllReportStatus() {
        var result = reportStatusService.findAll();
        return ApiResponse.<List<ReportStatusDto>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ReportStatusDto> getReportStatusById(@PathVariable("id") Long id) {
        var result = reportStatusService.findById(id);
        return ApiResponse.<ReportStatusDto>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ReportStatusDto> updateReportStatus(
            @PathVariable("id") Long id,
            @RequestBody @Valid ReportStatusDto dto) {
        var result = reportStatusService.update(id, dto);
        return ApiResponse.<ReportStatusDto>builder()
                .result(result)
                .message("Report status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteReportStatus(@PathVariable("id") Long id) {
        reportStatusService.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("Report status deleted successfully")
                .build();
    }
}
