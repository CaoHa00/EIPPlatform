package com.EIPplatform.controller.report;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportDTO;
import com.EIPplatform.model.dto.report.reportfield.ReportFilterRequest;
import com.EIPplatform.model.dto.report.report.UpdateReportRequest;
import com.EIPplatform.model.dto.report.reportfile.ReportFileDTO;
import com.EIPplatform.model.dto.report.statistics.ReportStatisticsDTO;
import com.EIPplatform.model.dto.report.workflow.ReviewReportRequest;
import com.EIPplatform.model.dto.report.workflow.SubmitReportRequest;
import com.EIPplatform.service.report.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    ReportService reportService;

    // ===== Basic CRUD =====

    @PostMapping
    public ApiResponse<ReportDTO> createReport(@RequestBody CreateReportRequest request) {
        return ApiResponse.<ReportDTO>builder()
                .result(reportService.createReport(request))
                .build();
    }

    @PutMapping("/{reportId}")
    public ApiResponse<ReportDTO> updateReport(@PathVariable UUID reportId, @RequestBody UpdateReportRequest request) {
        return ApiResponse.<ReportDTO>builder()
                .result(reportService.updateReport(reportId, request))
                .build();
    }

    @GetMapping("/{reportId}")
    public ApiResponse<ReportDTO> getReportById(@PathVariable UUID reportId) {
        return ApiResponse.<ReportDTO>builder()
                .result(reportService.getReportById(reportId))
                .build();
    }

    @GetMapping("/{reportId}/details")
    public ApiResponse<ReportDTO> getReportByIdWithDetails(@PathVariable UUID reportId) {
        return ApiResponse.<ReportDTO>builder()
                .result(reportService.getReportByIdWithDetails(reportId))
                .build();
    }

    @DeleteMapping("/{reportId}")
    public ApiResponse<Void> deleteReport( @PathVariable UUID reportId) {
        reportService.deleteReport(reportId);
        return ApiResponse.<Void>builder()
                .message("Report with id " + reportId + " deleted successfully")
                .build();
    }

    // ===== List & Filter =====

    @GetMapping("/business/{businessDetailId}")
    public ApiResponse<List<ReportDTO>> getReportsByBusiness(@PathVariable UUID businessDetailId, @RequestBody ReportFilterRequest filter) {
        return ApiResponse.<List<ReportDTO>>builder()
                .result(reportService.getReportsByBusiness(businessDetailId, filter))
                .build();
    }

    @GetMapping
    public ApiResponse<List<ReportDTO>> getAllReports(@RequestBody ReportFilterRequest filter) {
        return ApiResponse.<List<ReportDTO>>builder()
                .result(reportService.getAllReports(filter))
                .build();
    }

    // ===== Workflow =====

    @PostMapping("/submit")
    public ApiResponse<Void> submitReport(@RequestBody SubmitReportRequest request) {
        reportService.submitReport(request);
        return ApiResponse.<Void>builder()
                .message("Report submitted successfully")
                .build();
    }

    @PostMapping("/review")
    public ApiResponse<Void> reviewReport( @RequestBody ReviewReportRequest request) {
        reportService.reviewReport(request);
        return ApiResponse.<Void>builder()
                .message("Report reviewed successfully")
                .build();
    }

    @PostMapping("/{reportId}/new-version")
    public ApiResponse<ReportDTO> createNewVersion( @PathVariable UUID reportId) {
        return ApiResponse.<ReportDTO>builder()
                .result(reportService.createNewVersion(reportId))
                .build();
    }

    // ===== Analytics =====

    @GetMapping("/{reportId}/completion")
    public ApiResponse<BigDecimal> calculateCompletionPercentage(@PathVariable UUID reportId) {
        return ApiResponse.<BigDecimal>builder()
                .result(reportService.calculateCompletionPercentage(reportId))
                .build();
    }

    @GetMapping("/{reportId}/history")
    public ApiResponse<List<ReportDTO>> getReportVersionHistory(@PathVariable UUID reportId) {
        return ApiResponse.<List<ReportDTO>>builder()
                .result(reportService.getReportVersionHistory(reportId))
                .build();
    }

    @GetMapping("/{reportId}/export")
    public ResponseEntity<Resource> exportReport(@PathVariable UUID reportId, @RequestParam String format) {
        return ResponseEntity.ok(reportService.exportReport(reportId, format));
    }

    @GetMapping("/business/{businessDetailId}/statistics")
    public ApiResponse<ReportStatisticsDTO> getReportStatistics(@PathVariable UUID businessDetailId) {
        return ApiResponse.<ReportStatisticsDTO>builder()
                .result(reportService.getReportStatistics(businessDetailId))
                .build();
    }

    @GetMapping("/business/{businessDetailId}/upcoming")
    public ApiResponse<List<ReportDTO>> getUpcomingReports(@PathVariable UUID businessDetailId, @RequestParam(defaultValue = "30") int daysThreshold) {
        return ApiResponse.<List<ReportDTO>>builder()
                .result(reportService.getUpcomingReports(businessDetailId, daysThreshold))
                .build();
    }

    // ===== File Management =====

    @PostMapping("/{reportId}/files")
    public ApiResponse<ReportFileDTO> uploadFile(@PathVariable UUID reportId, @RequestParam MultipartFile file) {
        return ApiResponse.<ReportFileDTO>builder()
                .result(reportService.uploadFile(reportId, file))
                .build();
    }

    @DeleteMapping("/files/{fileId}")
    public ApiResponse<Void> deleteFile(@PathVariable Integer fileId) {
        reportService.deleteFile(fileId);
        return ApiResponse.<Void>builder()
                .message("File with id " + fileId + " deleted successfully")
                .build();
    }

    @GetMapping("/{reportId}/files/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID reportId, @PathVariable Integer fileId) {
        return ResponseEntity.ok(reportService.downloadFile(reportId, fileId));
    }

    @GetMapping("/{reportId}/files")
    public ApiResponse<List<ReportFileDTO>> getFilesByReport(@PathVariable UUID reportId) {
        return ApiResponse.<List<ReportFileDTO>>builder()
                .result(reportService.getFilesByReport(reportId))
                .build();
    }
}