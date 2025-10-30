package com.EIPplatform.controller.report;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportA05DTO;
import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.service.report.ReportA05Service;

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

    // 3. Lưu waste water vào CACHE
    @PutMapping("/{reportId}/wastewater/draft")
    public ResponseEntity<WasteWaterDataDTO> saveDraftWasteWaterData(
            @PathVariable UUID reportId,
            @Valid @RequestBody WasteWaterDataDTO data) {
        log.info("PUT /api/v1/reports/{}/wastewater/draft", reportId);
        WasteWaterDataDTO saved = reportA05Service.saveDraftWasteWaterData(reportId, data);
        return ResponseEntity.ok(saved);
    }
    // 4. Lấy waste water từ CACHE
    @GetMapping("/{reportId}/wastewater/draft")
    public ResponseEntity<WasteWaterDataDTO> getDraftWasteWaterData(
            @PathVariable UUID reportId) {
        log.info("GET /api/v1/reports/{}/wastewater/draft", reportId);
        WasteWaterDataDTO data = reportA05Service.getDraftWasteWaterData(reportId);
        
        if (data == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(data);
    }
}