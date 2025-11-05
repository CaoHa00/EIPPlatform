package com.EIPplatform.controller.report;

import com.EIPplatform.model.dto.report.report.*;
import com.EIPplatform.service.report.reporta05.ReportA05Service;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import org.apache.hc.core5.http.HttpStatus;
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
            @PathVariable UUID reportId) {
        log.info("GET /api/v1/reports/{}/draft", reportId);
        ReportA05DraftDTO data = reportA05Service.getDraftData(reportId);

        if (data == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }

    /**
     * Submit draft từ CACHE xuống DATABASE
     */
    @PostMapping("/{reportId}/draft/submit")
    public ResponseEntity<ReportA05DTO> submitDraftToDatabase(
            @PathVariable UUID reportId) {
        log.info("POST /api/v1/reports/{}/draft/submit", reportId);
        ReportA05DTO submitted = reportA05Service.submitDraftToDatabase(reportId);
        return ResponseEntity.ok(submitted);
    }

    /**
     * Cập nhật inspection remedy report cho report
     */
    @PatchMapping("/{reportId}/inspection-remedy-report")
    public ResponseEntity<InspectionRemedyResponse> updateInspectionRemedyReport(
            @PathVariable UUID reportId,
            @Valid @RequestBody UpdateInspectionRemedyReportRequest request) {
        log.info("PATCH /api/v1/reports/{}/inspection-remedy-report - Updating inspection remedy report", reportId);
        InspectionRemedyResponse updated = reportA05Service.updateInspectionRemedyReport(reportId, request);
        return ResponseEntity.ok(updated);
    }
}