package com.EIPplatform.controller.report.report05.airemmissionmanagement;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.service.report.reporta05.airemmissionmanagement.AirEmissionDataService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AirEmissionDataController {

    AirEmissionDataService airEmissionDataService;

    // ==================== AIR EMISSION DATA ENDPOINTS ====================

    @PostMapping(value = "/{reportId}/draft/air-emission", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AirEmissionDataDTO> createAirEmissionData(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId,
            @RequestPart("data")@Valid AirEmissionDataCreateDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        var result = airEmissionDataService.createAirEmissionData(reportId, userAccountId, request, file);
        return ApiResponse.<AirEmissionDataDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reportId}/draft/air-emission")
    public ApiResponse<AirEmissionDataDTO> getAirEmissionData(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        var result = airEmissionDataService.getAirEmissionData(reportId, userAccountId);
        return ApiResponse.<AirEmissionDataDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{reportId}/draft/air-emission")
    public ApiResponse<Void> deleteAirEmissionData(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        airEmissionDataService.deleteAirEmissionData(reportId, userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/{reportId}/draft/air-emission/file")
    public ApiResponse<Void> deleteAirEmissionDataFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        airEmissionDataService.deleteAirEmissionDataFile(reportId, userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/{reportId}/draft/air-emission/file/download")
    public ResponseEntity<Resource> downloadAirEmissionDataFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        Resource resource = airEmissionDataService.downloadAirEmissionDataFile(reportId, userAccountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{reportId}/draft/air-emission/file/exists")
    public ApiResponse<Boolean> hasAirEmissionDataFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        var result = airEmissionDataService.hasAirEmissionDataFile(reportId, userAccountId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }
}