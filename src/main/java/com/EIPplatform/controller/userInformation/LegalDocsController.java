package com.EIPplatform.controller.userInformation;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.legalDocs.LegalDocsCreationRequest;
import com.EIPplatform.model.dto.legalDocs.LegalDocsResponse;
import com.EIPplatform.model.dto.legalDocs.LegalDocsUpdateRequest;
import com.EIPplatform.service.userInformation.LegalDocsInterface;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/legal-docs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LegalDocsController {

    LegalDocsInterface legalDocsService;

    @PostMapping
    public ApiResponse<LegalDocsResponse> createLegalDoc(
            @RequestBody @Valid LegalDocsCreationRequest request) {

        LegalDocsResponse response = legalDocsService.createLegalDoc(request);

        return ApiResponse.<LegalDocsResponse>builder()
                .message("Legal document created successfully")
                .result(response)
                .build();
    }

    @PutMapping
    public ApiResponse<LegalDocsResponse> updateLegalDoc(
            @RequestBody @Valid LegalDocsUpdateRequest request) {

        LegalDocsResponse response = legalDocsService.updateLegalDoc(request);

        return ApiResponse.<LegalDocsResponse>builder()
                .message("Legal document updated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/{legalDocId}")
    public ApiResponse<LegalDocsResponse> getLegalDocById(
            @PathVariable UUID legalDocId) {

        LegalDocsResponse response = legalDocsService.getLegalDocById(legalDocId);

        return ApiResponse.<LegalDocsResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<LegalDocsResponse>> getAllLegalDocs() {

        List<LegalDocsResponse> response = legalDocsService.getAllLegalDocs();

        return ApiResponse.<List<LegalDocsResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/investor-organization/{investorOrganizationId}")
    public ApiResponse<List<LegalDocsResponse>> getLegalDocsByInvestorOrganization(
            @PathVariable UUID investorOrganizationId) {

        List<LegalDocsResponse> response = legalDocsService
                .getLegalDocsByInvestorOrganization(investorOrganizationId);

        return ApiResponse.<List<LegalDocsResponse>>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{legalDocId}")
    public ApiResponse<Void> deleteLegalDoc(
            @PathVariable UUID legalDocId) {

        legalDocsService.deleteLegalDoc(legalDocId);

        return ApiResponse.<Void>builder()
                .message("Legal document deleted successfully")
                .build();
    }
}