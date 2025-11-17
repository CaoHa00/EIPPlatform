package com.EIPplatform.controller.businessInformation;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.service.businessInformation.BusinessDetailInterface;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/all")
    public ApiResponse<List<BusinessDetailResponse>> getAllBusinessDetails() {
        var result = businessDetailService.findAll();
        return ApiResponse.<List<BusinessDetailResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping
    public ApiResponse<BusinessDetailResponse> getBusinessDetailById(
            @RequestParam UUID userAccountId) {
        var result = businessDetailService.findByUserAccountId(userAccountId);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{userAccountId}")
    public ApiResponse<UUID> getBusinessDetailByUserAccountId(@PathVariable UUID userAccountId) {
        return ApiResponse.<UUID>builder()
                .result(businessDetailService.findByBusinessDetailId(userAccountId))
                .build();
    }

//    @GetMapping("/with-history")
//    public ApiResponse<BusinessDetailWithHistoryConsumptionDTO> getBusinessDetailWithHistory(
//            @RequestParam UUID userAccountId) {
//        var result = businessDetailService.getBusinessDetailWithHistoryConsumption(userAccountId);
//        return ApiResponse.<BusinessDetailWithHistoryConsumptionDTO>builder()
//                .result(result)
//                .build();
//    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ApiResponse<BusinessDetailResponse> createBusinessDetail(
            @RequestParam UUID userAccountId,
            @RequestPart("data") BusinessDetailDTO dto,
            @RequestPart(value = "isoFile", required = false) MultipartFile isoFile) {
        var result = businessDetailService.createBusinessDetail(userAccountId, dto, isoFile);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BusinessDetailResponse> updateBusinessDetail(
            @RequestParam UUID userAccountId,
            @RequestPart("data") BusinessDetailDTO dto,
            @RequestPart(value = "isoFile", required = false) MultipartFile isoFile) {
        var result = businessDetailService.updateBusinessDetail(userAccountId, dto, isoFile);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteBusinessDetail(
            @RequestParam UUID userAccountId) {
        businessDetailService.deleteByUserAccountId(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    // ==================== ISO CERTIFICATE FILE ENDPOINTS ====================

    @PostMapping(value = "/iso-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BusinessDetailResponse> uploadIsoCertificateFile(
            @RequestParam UUID businessDetailId,
            @RequestPart("file") MultipartFile file) {
        var result = businessDetailService.uploadIsoCertificateFile(businessDetailId, file);
        return ApiResponse.<BusinessDetailResponse>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/iso-file")
    public ApiResponse<Void> deleteIsoCertificateFile(
            @RequestParam UUID userAccountId) {
        businessDetailService.deleteIsoCertificateFile(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/iso-file/exists")
    public ApiResponse<Boolean> hasIsoCertificateFile(
            @RequestParam UUID userAccountId) {
        var result = businessDetailService.hasIsoCertificateFile(userAccountId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }
}