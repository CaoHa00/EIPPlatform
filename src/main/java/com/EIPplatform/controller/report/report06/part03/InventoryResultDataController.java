package com.EIPplatform.controller.report.report06.part03;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataDTO;
import com.EIPplatform.service.report.report06.part03.InventoryResultDataService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/report06")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class InventoryResultDataController {

    InventoryResultDataService inventoryResultDataService;

    @PostMapping(value = "/{report06Id}/draft/inventory-result")
    public ApiResponse<InventoryResultDataDTO> createInventoryResultData(
            @PathVariable UUID report06Id,
            @RequestParam UUID userAccountId,
            @RequestBody @Valid InventoryResultDataCreateDTO request) {

        var result = inventoryResultDataService.createInventoryResultData(report06Id, userAccountId, request);
        return ApiResponse.<InventoryResultDataDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping(value = "/{report06Id}/draft/inventory-result")
    public ApiResponse<InventoryResultDataDTO> getInventoryResultData(
            @PathVariable UUID report06Id,
            @RequestParam UUID userAccountId) {

        var result = inventoryResultDataService.getInventoryResultData(report06Id, userAccountId);
        return ApiResponse.<InventoryResultDataDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping(value = "/{report06Id}/draft/inventory-result")
    public ApiResponse<String> deleteInventoryResultData(
            @PathVariable UUID report06Id,
            @RequestParam UUID userAccountId) {

        inventoryResultDataService.deleteInventoryResultData(report06Id, userAccountId);
        return ApiResponse.<String>builder()
                .result("Inventory Result Data has been deleted from draft")
                .build();
    }
}