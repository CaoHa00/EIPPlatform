package com.EIPplatform.controller.report.report05.wastemanagement;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.service.report.reporta05.wastemanagement.WasteManagementDataService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class WasteManagementDataController {

    WasteManagementDataService wasteManagementDataService;

    // ==================== WASTE MANAGEMENT DATA ENDPOINTS ====================

    @PostMapping("/{reportId}/draft/wastemanagement")
    public ApiResponse<WasteManagementDataDTO> createWasteManagementData(
            @PathVariable UUID reportId,
            @RequestBody @Valid WasteManagementDataCreateDTO request) {
        var result = wasteManagementDataService.createWasteManagementData(reportId, request);
        return ApiResponse.<WasteManagementDataDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{reportId}/draft/wastemanagement")
    public ApiResponse<WasteManagementDataDTO> getWasteManagementData(
            @PathVariable UUID reportId) {
        var result = wasteManagementDataService.getWasteManagementData(reportId);
        return ApiResponse.<WasteManagementDataDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{reportId}/draft/wastemanagement")
    public ApiResponse<Void> deleteWasteManagementData(
            @PathVariable UUID reportId) {
        wasteManagementDataService.deleteWasteManagementData(reportId);
        return ApiResponse.<Void>builder()
                .build();
    }
}