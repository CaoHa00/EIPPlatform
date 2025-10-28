package com.EIPplatform.controller.report;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import com.EIPplatform.service.report.reportstatus.ReportStatusInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/report-status")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReportStatusController {
    ReportStatusInterface reportStatusService;

    @PostMapping
    ApiResponse<ReportStatusDTO> createReportStatus(@RequestBody ReportStatusDTO dto) {
        var result = reportStatusService.create(dto);
        return ApiResponse.<ReportStatusDTO>builder()
                .result(result)
                .message("Report status created successfully")
                .build();
    }

    @GetMapping
    ApiResponse<List<ReportStatusDTO>> getAllReportStatus() {
        var result = reportStatusService.findAll();
        return ApiResponse.<List<ReportStatusDTO>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<ReportStatusDTO> getReportStatusById(@PathVariable("id") Long id) {
        var result = reportStatusService.findById(id);
        return ApiResponse.<ReportStatusDTO>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<ReportStatusDTO> updateReportStatus(
            @PathVariable("id") Long id,
            @RequestBody ReportStatusDTO dto) {
        var result = reportStatusService.update(id, dto);
        return ApiResponse.<ReportStatusDTO>builder()
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
