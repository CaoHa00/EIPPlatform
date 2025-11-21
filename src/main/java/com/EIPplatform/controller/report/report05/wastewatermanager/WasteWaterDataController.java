package com.EIPplatform.controller.report.report05.wastewatermanager;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.service.report.reporta05.wastewatermanager.WasteWaterDataService;
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
public class WasteWaterDataController {

    WasteWaterDataService wasteWaterDataService;

    // ==================== WASTE WATER DATA ENDPOINTS ====================

    @PostMapping(value = "/{reportId}/draft/waste-water", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<WasteWaterDataDTO> createWasteWaterData(
            @PathVariable UUID reportId,
            @RequestParam UUID businessDetailId,
            @RequestPart("data") WasteWaterDataCreateDTO request,
            @RequestPart(value = "connectionFile", required = false) MultipartFile connectionFile,
            @RequestPart(value = "mapFile", required = false) MultipartFile mapFile) {
        var result = wasteWaterDataService.createWasteWaterData(reportId, businessDetailId, request, connectionFile, mapFile);
        return ApiResponse.<WasteWaterDataDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reportId}/draft/waste-water")
    public ApiResponse<WasteWaterDataDTO> getWasteWaterData(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        var result = wasteWaterDataService.getWasteWaterData(reportId, userAccountId);
        return ApiResponse.<WasteWaterDataDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{reportId}/draft/waste-water")
    public ApiResponse<Void> deleteWasteWaterData(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        wasteWaterDataService.deleteWasteWaterData(reportId, userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/{reportId}/draft/waste-water/connection-file")
    public ApiResponse<Void> deleteWasteWaterDataConnectionFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        wasteWaterDataService.deleteWasteWaterDataConnectionFile(reportId, userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/{reportId}/draft/waste-water/map-file")
    public ApiResponse<Void> deleteWasteWaterDataMapFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        wasteWaterDataService.deleteWasteWaterDataMapFile(reportId, userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/{reportId}/draft/waste-water/connection-file/download")
    public ResponseEntity<Resource> downloadWasteWaterDataConnectionFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        Resource resource = wasteWaterDataService.downloadWasteWaterDataConnectionFile(reportId, userAccountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{reportId}/draft/waste-water/map-file/download")
    public ResponseEntity<Resource> downloadWasteWaterDataMapFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        Resource resource = wasteWaterDataService.downloadWasteWaterDataMapFile(reportId, userAccountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/{reportId}/draft/waste-water/connection-file/exists")
    public ApiResponse<Boolean> hasWasteWaterDataConnectionFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        var result = wasteWaterDataService.hasWasteWaterDataConnectionFile(reportId, userAccountId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reportId}/draft/waste-water/map-file/exists")
    public ApiResponse<Boolean> hasWasteWaterDataMapFile(
            @PathVariable UUID reportId,
            @RequestParam UUID userAccountId) {
        var result = wasteWaterDataService.hasWasteWaterDataMapFile(reportId, userAccountId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }
}