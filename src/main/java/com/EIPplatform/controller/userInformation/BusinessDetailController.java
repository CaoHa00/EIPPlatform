package com.EIPplatform.controller.userInformation;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;
import com.EIPplatform.service.userInformation.BusinessDetailInterface;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/business-details")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BusinessDetailController {

    BusinessDetailInterface businessDetailService;

    // ==================== BUSINESS DETAIL ENDPOINTS ====================

    @GetMapping
    public ApiResponse<List<BusinessDetailResponse>> getAllBusinessDetails(
            @RequestParam UUID userAccountId) {
        var result = businessDetailService.findAll();
        return ApiResponse.<List<BusinessDetailResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BusinessDetailResponse> getBusinessDetailById(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id) {
        var result = businessDetailService.findByBusinessDetailId(id);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}/with-history")
    public ApiResponse<BusinessDetailWithHistoryConsumptionDTO> getBusinessDetailWithHistory(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id) {
        var result = businessDetailService.getBusinessDetailWithHistoryConsumption(id);
        return ApiResponse.<BusinessDetailWithHistoryConsumptionDTO>builder()
                .result(result)
                .build();
    }

    @PostMapping
    public ApiResponse<BusinessDetailResponse> createBusinessDetail(
            @RequestParam UUID userAccountId,
            @RequestBody @Valid BusinessDetailDTO dto) {
        var result = businessDetailService.createBusinessDetail(userAccountId, dto);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BusinessDetailResponse> updateBusinessDetail(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id,
            @RequestBody @Valid BusinessDetailDTO dto) {
        var result = businessDetailService.updateBusinessDetail(id, dto);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBusinessDetail(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id) {
        businessDetailService.deleteByBusinessDetailId(id);
        return ApiResponse.<Void>builder()
                .build();
    }
}