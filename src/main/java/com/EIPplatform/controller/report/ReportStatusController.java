package com.EIPplatform.controller.report;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import com.EIPplatform.service.report.reportstatus.ReportStatusInterface;

@RestController
@RequestMapping("/api/report-status")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReportStatusController {

    ReportStatusInterface reportStatusService;

    @PostMapping
    public ApiResponse<ReportStatusDTO> createReportStatus(@RequestBody ReportStatusDTO dto) {
        var result = reportStatusService.create(dto);
        return ApiResponse.<ReportStatusDTO>builder()
                .result(result)
                .message("Report status created successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<List<ReportStatusDTO>> getAllReportStatus() {
        var result = reportStatusService.findAll();
        return ApiResponse.<List<ReportStatusDTO>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ReportStatusDTO> getReportStatusById(@PathVariable("id") Integer id) {
        var result = reportStatusService.findById(id);
        return ApiResponse.<ReportStatusDTO>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ReportStatusDTO> updateReportStatus(
            @PathVariable("id") Integer id,
            @RequestBody ReportStatusDTO dto) {
        var result = reportStatusService.update(id, dto);
        return ApiResponse.<ReportStatusDTO>builder()
                .result(result)
                .message("Report status updated successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteReportStatus(@PathVariable("id") Integer id) {
        reportStatusService.deleteById(id);
        return ApiResponse.<Void>builder()
                .message("Report status deleted successfully")
                .build();
    }
}
