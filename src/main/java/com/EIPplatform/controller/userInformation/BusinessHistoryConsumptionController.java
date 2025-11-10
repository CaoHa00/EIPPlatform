package com.EIPplatform.controller.userInformation;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionCreateDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionUpdateDTO;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.service.userInformation.BusinessHistoryConsumptionInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business-history-consumptions")
@RequiredArgsConstructor
public class BusinessHistoryConsumptionController {

    private final BusinessHistoryConsumptionInterface businessHistoryConsumptionService;

    @GetMapping("/business/{businessDetailId}")
    public ResponseEntity<ApiResponse<List<BusinessHistoryConsumptionDTO>>> getByBusinessDetailId(
            @PathVariable UUID businessDetailId,
            @AuthenticationPrincipal UserAccount currentUser) {
        var result = businessHistoryConsumptionService.findByBusinessDetailId(businessDetailId, currentUser.getUserAccountId());
        return ResponseEntity.ok(ApiResponse.<List<BusinessHistoryConsumptionDTO>>builder()
                .result(result)
                .build());
    }

    @PostMapping("/business/{userAccountId}")
    public ResponseEntity<ApiResponse<BusinessHistoryConsumptionDTO>> create(
            @PathVariable UUID userAccountId,
            @RequestBody @Valid BusinessHistoryConsumptionCreateDTO dto) {
        var result = businessHistoryConsumptionService.createBusinessHistoryConsumption(userAccountId, dto);
        return ResponseEntity.ok(ApiResponse.<BusinessHistoryConsumptionDTO>builder()
                .result(result)
                .build());
    }

    @PutMapping("/{businessHistoryConsumptionId}")
    public ResponseEntity<ApiResponse<BusinessHistoryConsumptionDTO>> update(
            @PathVariable UUID businessHistoryConsumptionId,
            @RequestBody @Valid BusinessHistoryConsumptionUpdateDTO dto,
            @AuthenticationPrincipal UserAccount currentUser) {
        var result = businessHistoryConsumptionService.updateBusinessHistoryConsumption(currentUser.getUserAccountId(), businessHistoryConsumptionId, dto);
        return ResponseEntity.ok(ApiResponse.<BusinessHistoryConsumptionDTO>builder()
                .result(result)
                .build());
    }
}