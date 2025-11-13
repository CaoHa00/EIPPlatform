package com.EIPplatform.controller.report.report05;

import com.EIPplatform.model.dto.report.report05.*;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.service.report.reporta05.ReportA05Service;
import com.EIPplatform.util.SessionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportA05Controller {

    ReportA05Service reportA05Service;

    /**
     * Tạo report mới
     */
    @PostMapping
    public ResponseEntity<ReportA05DTO> createReport(
            @Valid @RequestBody CreateReportRequest request) {

        log.info("POST /api/v1/reports - Creating report");
        ReportA05DTO created = reportA05Service.createReport(request);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(created);
    }

    /**
     * Lấy report theo ID
     */
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportA05DTO> getReportById(
            @PathVariable("reportId") java.util.UUID reportId) {

        log.info("GET /api/v1/reports/{} - Getting report by ID", reportId);
        ReportA05DTO report = reportA05Service.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    /**
     * Lấy draft data từ CACHE
     */
    @GetMapping("/{reportId}/draft")
    public ResponseEntity<ReportA05DraftDTO> getDraftData(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        log.info("GET /api/v1/reports/{}/draft - userAccountId: {}", reportId, userAccountId);
        ReportA05DraftDTO data = reportA05Service.getDraftData(reportId, userAccountId);

        if (data == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }

    /**
     * Cập nhật completion percentage cho draft
     */
    @PostMapping("/{reportId}/draft/completion")
    public ResponseEntity<ReportA05DraftDTO> updateDraftCompletion(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        log.info("POST /api/v1/reports/{}/draft/completion - userAccountId: {}", reportId, userAccountId);
        ReportA05DraftDTO updated = reportA05Service.updateDraftCompletion(reportId, userAccountId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Submit draft từ CACHE xuống DATABASE
     */
    @PostMapping("/{reportId}/draft/submit")
    public ResponseEntity<ReportA05DTO> submitDraftToDatabase(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        log.info("POST /api/v1/reports/{}/draft/submit - userAccountId: {}", reportId, userAccountId);
        ReportA05DTO submitted = reportA05Service.submitDraftToDatabase(reportId, userAccountId);
        return ResponseEntity.ok(submitted);
    }

    /**
     * Cập nhật inspection remedy report cho report
     */
    @PutMapping("/{reportId}/inspection-remedy-report")
    public ResponseEntity<InspectionRemedyResponse> updateInspectionRemedyReport(
            @PathVariable UUID reportId,
            @Valid @RequestBody UpdateInspectionRemedyReportRequest request) {
        log.info("PUT /api/v1/reports/{}/inspection-remedy-report - Updating inspection remedy report", reportId);
        InspectionRemedyResponse updated = reportA05Service.updateInspectionRemedyReport(reportId, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/export/{reportId}")
    public ResponseEntity<byte[]> exportReport(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        try {
            log.info(" Starting report export for reportId: {} - userAccountId: {}", reportId, userAccountId);

            // 1. Generate report file
            byte[] fileBytes = reportA05Service.generateReportFile(reportId, userAccountId);

            if (fileBytes == null || fileBytes.length == 0) {
                log.error(" Generated file is empty for reportId: {}", reportId);
                return ResponseEntity.noContent().build();
            }

            // 2. Get report info để tạo tên file
            String fileName = generateFileName(reportId);

            // 3. Encode tên file để hỗ trợ tiếng Việt
            String encodedFileName = encodeFileName(fileName);

            log.info(" Report exported successfully: {} ({} bytes)", fileName, fileBytes.length);

            // 4. Return response với headers chuẩn
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileBytes.length))
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(fileBytes);

        } catch (Exception e) {
            log.error(" Error exporting report for reportId: {}", reportId, e);
            return ResponseEntity.internalServerError()
                    .body(("Error exporting report: " + e.getMessage()).getBytes(StandardCharsets.UTF_8));
        }
    }

    private String generateFileName(UUID reportId) {
        try {
            // Lấy thông tin report
            var report = reportA05Service.getReportById(reportId);

            // Timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            // Tên doanh nghiệp (sanitize - loại bỏ ký tự đặc biệt)
            String facilityName = report.getFacilityName() != null
                    ? sanitizeFileName(report.getFacilityName())
                    : "Unknown";

            // Năm báo cáo
            String year = report.getReportYear() != null
                    ? report.getReportYear().toString()
                    : "0000";

            // Build tên file
            return String.format("BaoCaoA05_%s_%s_%s.docx",
                    facilityName,
                    year,
                    timestamp);

        } catch (Exception e) {
            log.warn(" Could not generate dynamic filename, using default: {}", e.getMessage());
            // Fallback: Tên file đơn giản với timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            return String.format("BaoCaoA05_%s_%s.docx", reportId.toString().substring(0, 8), timestamp);
        }
    }

    private String sanitizeFileName(String input) {
        if (input == null || input.isEmpty()) {
            return "Unknown";
        }

        // Giữ chữ cái, số, tiếng Việt, dấu cách và gạch ngang
        // Loại bỏ: / \ : * ? " < > |
        String sanitized = input
                .replaceAll("[/\\\\:*?\"<>|]", "") // Loại bỏ ký tự không hợp lệ
                .replaceAll("\\s+", "_") // Thay khoảng trắng = underscore
                .trim();

        // Giới hạn độ dài (max 50 ký tự để tránh tên file quá dài)
        if (sanitized.length() > 50) {
            sanitized = sanitized.substring(0, 50);
        }

        return sanitized;
    }

    private String encodeFileName(String fileName) {
        try {
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replaceAll("\\+", "%20"); // Space = %20 thay vì +
        } catch (UnsupportedEncodingException e) {
            log.warn(" Could not encode filename, using original: {}", fileName);
            return fileName;
        }
    }
}