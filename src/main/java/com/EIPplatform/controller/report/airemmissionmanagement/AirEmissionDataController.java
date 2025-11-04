package com.EIPplatform.controller.report.airemmissionmanagement;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataUpdateDTO;
import com.EIPplatform.service.report.airemmissionmanagement.AirEmissionDataService;
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
            @RequestPart("data") AirEmissionDataCreateDTO request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        var result = airEmissionDataService.createAirEmissionData(reportId, request, file);
        return ApiResponse.<AirEmissionDataDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reportId}/draft/air-emission")
    public ApiResponse<AirEmissionDataDTO> getAirEmissionData(
            @PathVariable UUID reportId) {
        var result = airEmissionDataService.getAirEmissionData(reportId);
        return ApiResponse.<AirEmissionDataDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{reportId}/draft/air-emission")
    public ApiResponse<Void> deleteAirEmissionData(
            @PathVariable UUID reportId) {
        airEmissionDataService.deleteAirEmissionData(reportId);
        return ApiResponse.<Void>builder()
                .build();
    }

//    @PostMapping(value = "/{reportId}/draft/air-emission/file", consumes = {"multipart/form-data"})
//    public ApiResponse<AirEmissionDataDTO> uploadAirEmissionDataFile(
//            @PathVariable UUID reportId,
//            @RequestParam("file") MultipartFile file) { // ✅ Changed to @RequestParam (only file)
//        var result = airEmissionDataService.uploadAirEmissionDataFile(reportId, file);
//        return ApiResponse.<AirEmissionDataDTO>builder()
//                .result(result)
//                .build();
//    }

    @DeleteMapping("/{reportId}/draft/air-emission/file")
    public ApiResponse<Void> deleteAirEmissionDataFile(
            @PathVariable UUID reportId) {
        airEmissionDataService.deleteAirEmissionDataFile(reportId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/{reportId}/draft/air-emission/file/download")
    public ResponseEntity<Resource> downloadAirEmissionDataFile(
            @PathVariable UUID reportId) {
        Resource resource = airEmissionDataService.downloadAirEmissionDataFile(reportId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{reportId}/draft/air-emission/file/exists")
    public ApiResponse<Boolean> hasAirEmissionDataFile(
            @PathVariable UUID reportId) {
        var result = airEmissionDataService.hasAirEmissionDataFile(reportId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }
}