package com.EIPplatform.controller.userInformation;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;
import com.EIPplatform.service.userInformation.BusinessDetailInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = {"multipart/form-data"})
    public ApiResponse<BusinessDetailResponse> createBusinessDetail(
            @RequestParam UUID userAccountId,
            @RequestBody BusinessDetailDTO dto,
            @RequestParam(value = "isoFile", required = false) MultipartFile isoFile) {
        var result = businessDetailService.createBusinessDetail(userAccountId, dto, isoFile);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<BusinessDetailResponse> updateBusinessDetail(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id,
            @RequestBody BusinessDetailDTO dto,
            @RequestParam(value = "isoFile", required = false) MultipartFile isoFile) {
        var result = businessDetailService.updateBusinessDetail(id, dto, isoFile);
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

    // ==================== ISO CERTIFICATE FILE ENDPOINTS ====================

    @PostMapping(value = "/{id}/iso-file", consumes = {"multipart/form-data"})
    public ApiResponse<BusinessDetailResponse> uploadIsoCertificateFile(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id,
            @RequestParam("file") MultipartFile file) {
        var result = businessDetailService.uploadIsoCertificateFile(id, file);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/{id}/iso-file")
    public ApiResponse<Void> deleteIsoCertificateFile(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id) {
        businessDetailService.deleteIsoCertificateFile(id);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/{id}/iso-file/exists")
    public ApiResponse<Boolean> hasIsoCertificateFile(
            @RequestParam UUID userAccountId,
            @PathVariable UUID id) {
        var result = businessDetailService.hasIsoCertificateFile(id);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }
}