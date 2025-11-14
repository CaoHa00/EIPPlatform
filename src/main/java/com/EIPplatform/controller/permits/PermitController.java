package com.EIPplatform.controller.permits;


import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.permitshistory.*;
import com.EIPplatform.service.permits.PermitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/permits")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PermitController {

    PermitService permitService;
    ObjectMapper objectMapper;

    // ==================== ENV PERMITS ENDPOINTS ====================

    @PostMapping(value = "/envpermits", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EnvPermitDTO> createEnvPermit(
            @RequestParam UUID userAccountId,
            @RequestPart("data")@Valid CreateMainPermitRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        var result = permitService.createEnvPermit(userAccountId, request, file);
        return ApiResponse.<EnvPermitDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/envpermits")
    public ApiResponse<EnvPermitDTO> getEnvPermit(@RequestParam UUID userAccountId) {
        var result = permitService.getEnvPermit(userAccountId);
        return ApiResponse.<EnvPermitDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/envpermits/exists")
    public ApiResponse<Boolean> hasEnvPermit(@RequestParam UUID userAccountId) {
        var result = permitService.hasEnvPermit(userAccountId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }

    @PutMapping(value = "/envpermits", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EnvPermitDTO> updateEnvPermit(
            @RequestParam UUID userAccountId,
            @RequestPart("data") UpdateEnvPermitRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        var result = permitService.updateEnvPermit(userAccountId, request, file);
        return ApiResponse.<EnvPermitDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/envpermits")
    public ApiResponse<Void> deleteEnvPermit(@RequestParam UUID userAccountId) {
        permitService.deleteEnvPermit(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/envpermits/activate")
    public ApiResponse<Void> activateEnvPermit(@RequestParam UUID userAccountId) {
        permitService.activateEnvPermit(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/envpermits/deactivate")
    public ApiResponse<Void> deactivateEnvPermit(@RequestParam UUID userAccountId) {
        permitService.deactivateEnvPermit(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping(value = "/envpermits/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EnvPermitDTO> uploadEnvPermitFile(
            @RequestParam UUID userAccountId,
            @RequestPart("file") MultipartFile file) {
        var result = permitService.uploadEnvPermitFile(userAccountId, file);
        return ApiResponse.<EnvPermitDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/envpermits/file")
    public ApiResponse<Void> deleteEnvPermitFile(@RequestParam UUID userAccountId) {
        permitService.deleteEnvPermitFile(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/envpermits/file/download")
    public ResponseEntity<Resource> downloadEnvPermitFile(@RequestParam UUID userAccountId) {
        Resource resource = permitService.downloadEnvPermitFile(userAccountId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/envpermits/file/exists")
    public ApiResponse<Boolean> hasEnvPermitFile(@RequestParam UUID userAccountId) {
        var result = permitService.hasEnvPermitFile(userAccountId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }

    // ==================== COMPONENT PERMIT ENDPOINTS ====================

    @PostMapping(value = "/component", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EnvComponentPermitDTO> createComponentPermit(
            @RequestParam UUID userAccountId,
            @RequestPart("data") CreateComponentPermitRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        var result = permitService.createComponentPermit(userAccountId, request, file);
        return ApiResponse.<EnvComponentPermitDTO>builder()
                .result(result)
                .build();
    }

    @PostMapping(value = "/components/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<EnvComponentPermitDTO>> createComponentPermits(
            @RequestParam UUID userAccountId,
            @RequestPart("data") List<CreateComponentPermitRequest> requests,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {

        if (requests.isEmpty()) {
            throw new IllegalArgumentException("Requests list cannot be empty");
        }

        var results = permitService.createComponentPermits(userAccountId, requests, files);
        return ApiResponse.<List<EnvComponentPermitDTO>>builder()
                .result(results)
                .build();
    }

//    @PostMapping(value = "/component/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResponse<List<EnvComponentPermitDTO>> createMultipleComponentPermits(
//            @RequestParam UUID userAccountId,
//            @RequestBody @Valid List<CreateComponentPermitRequest> requests) {
//        var result = permitService.createMultipleComponentPermits(userAccountId, requests);
//        return ApiResponse.<List<EnvComponentPermitDTO>>builder()
//                .result(result)
//                .build();
//    }

    @GetMapping("/component/{permitId}")
    public ApiResponse<EnvComponentPermitDTO> getComponentPermitById(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        var result = permitService.getComponentPermitById(userAccountId, permitId);
        return ApiResponse.<EnvComponentPermitDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/component")
    public ApiResponse<List<EnvComponentPermitDTO>> getAllComponentPermits(
            @RequestParam UUID userAccountId) {
        var result = permitService.getAllComponentPermits(userAccountId);
        return ApiResponse.<List<EnvComponentPermitDTO>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/component/type/{permitType}")
    public ApiResponse<List<EnvComponentPermitDTO>> getComponentPermitsByType(
            @RequestParam UUID userAccountId,
            @PathVariable String permitType) {
        var result = permitService.getComponentPermitsByType(userAccountId, permitType);
        return ApiResponse.<List<EnvComponentPermitDTO>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/component/active")
    public ApiResponse<List<EnvComponentPermitDTO>> getActiveComponentPermits(
            @RequestParam UUID userAccountId) {
        var result = permitService.getActiveComponentPermits(userAccountId);
        return ApiResponse.<List<EnvComponentPermitDTO>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/component/inactive")
    public ApiResponse<List<EnvComponentPermitDTO>> getInactiveComponentPermits(
            @RequestParam UUID userAccountId) {
        var result = permitService.getInactiveComponentPermits(userAccountId);
        return ApiResponse.<List<EnvComponentPermitDTO>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/component/search")
    public ApiResponse<List<EnvComponentPermitDTO>> searchComponentPermits(
            @RequestParam UUID userAccountId,
            @RequestParam String keyword) {
        var result = permitService.searchComponentPermits(userAccountId, keyword);
        return ApiResponse.<List<EnvComponentPermitDTO>>builder()
                .result(result)
                .build();
    }

    @PutMapping(value = "/component/{permitId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EnvComponentPermitDTO> updateComponentPermit(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId,
            @RequestPart("data") UpdateComponentPermitRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        var result = permitService.updateComponentPermit(userAccountId, permitId, request, file);
        return ApiResponse.<EnvComponentPermitDTO>builder()
                .result(result)
                .build();
    }

    @PostMapping("/component/{permitId}/activate")
    public ApiResponse<Void> activateComponentPermit(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        permitService.activateComponentPermit(userAccountId, permitId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/component/{permitId}/deactivate")
    public ApiResponse<Void> deactivateComponentPermit(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        permitService.deactivateComponentPermit(userAccountId, permitId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping("/component/{permitId}/toggle")
    public ApiResponse<Void> toggleComponentPermitStatus(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        permitService.toggleComponentPermitStatus(userAccountId, permitId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping(value = "/component/activate/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Void> activateMultipleComponentPermits(
            @RequestParam UUID userAccountId,
            @RequestBody List<Long> permitIds) {
        permitService.activateMultipleComponentPermits(userAccountId, permitIds);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping(value = "/component/deactivate/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Void> deactivateMultipleComponentPermits(
            @RequestParam UUID userAccountId,
            @RequestBody List<Long> permitIds) {
        permitService.deactivateMultipleComponentPermits(userAccountId, permitIds);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/component/{permitId}")
    public ApiResponse<Void> deleteComponentPermit(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        permitService.deleteComponentPermit(userAccountId, permitId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping(value = "/component/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Void> deleteMultipleComponentPermits(
            @RequestParam UUID userAccountId,
            @RequestBody List<Long> permitIds) {
        permitService.deleteMultipleComponentPermits(userAccountId, permitIds);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/component/all")
    public ApiResponse<Void> deleteAllComponentPermits(@RequestParam UUID userAccountId) {
        permitService.deleteAllComponentPermits(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @DeleteMapping("/component/inactive")
    public ApiResponse<Void> deleteInactiveComponentPermits(@RequestParam UUID userAccountId) {
        permitService.deleteInactiveComponentPermits(userAccountId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @PostMapping(value = "/component/{permitId}/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<EnvComponentPermitDTO> uploadComponentPermitFile(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId,
            @RequestPart("file") MultipartFile file) {
        var result = permitService.uploadComponentPermitFile(userAccountId, permitId, file);
        return ApiResponse.<EnvComponentPermitDTO>builder()
                .result(result)
                .build();
    }

    @DeleteMapping("/component/{permitId}/file")
    public ApiResponse<Void> deleteComponentPermitFile(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        permitService.deleteComponentPermitFile(userAccountId, permitId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/component/{permitId}/file/download")
    public ResponseEntity<Resource> downloadComponentPermitFile(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        Resource resource = permitService.downloadComponentPermitFile(userAccountId, permitId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/component/{permitId}/file/exists")
    public ApiResponse<Boolean> hasComponentPermitFile(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        var result = permitService.hasComponentPermitFile(userAccountId, permitId);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }

    // ==================== STATISTICS & VALIDATION ====================

    @GetMapping("/statistics")
    public ApiResponse<PermitStatisticsDTO> getPermitStatistics(
            @RequestParam UUID userAccountId) {
        var result = permitService.getPermitStatistics(userAccountId);
        return ApiResponse.<PermitStatisticsDTO>builder()
                .result(result)
                .build();
    }

    @GetMapping("/component/permit-number/unique")
    public ApiResponse<Boolean> isComponentPermitNumberUnique(
            @RequestParam UUID userAccountId,
            @RequestParam String permitNumber) {
        var result = permitService.isComponentPermitNumberUnique(userAccountId, permitNumber);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }

    @PostMapping("/component/{permitId}/validate-ownership")
    public ApiResponse<Void> validateComponentPermitOwnership(
            @RequestParam UUID userAccountId,
            @PathVariable Long permitId) {
        permitService.validateComponentPermitOwnership(userAccountId, permitId);
        return ApiResponse.<Void>builder()
                .build();
    }
}