package com.EIPplatform.controller.userInformation;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.legalDoc.LegalDocCreationRequest;
import com.EIPplatform.model.dto.legalDoc.LegalDocResponse;
import com.EIPplatform.model.dto.legalDoc.LegalDocUpdateRequest;
import com.EIPplatform.service.userInformation.LegalDocsInterface;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/legal-docs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LegalDocController {

    LegalDocsInterface legalDocsService;

    @PostMapping
    public ApiResponse<LegalDocResponse> createLegalDoc(
            @RequestBody @Valid LegalDocCreationRequest request) {

        LegalDocResponse response = legalDocsService.createLegalDoc(request);

        return ApiResponse.<LegalDocResponse>builder()
                .message("Legal document created successfully")
                .result(response)
                .build();
    }

    @PutMapping
    public ApiResponse<LegalDocResponse> updateLegalDoc(
            @RequestBody @Valid LegalDocUpdateRequest request) {

        LegalDocResponse response = legalDocsService.updateLegalDoc(request);

        return ApiResponse.<LegalDocResponse>builder()
                .message("Legal document updated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/{legalDocId}")
    public ApiResponse<LegalDocResponse> getLegalDocById(
            @PathVariable UUID legalDocId) {

        LegalDocResponse response = legalDocsService.getLegalDocById(legalDocId);

        return ApiResponse.<LegalDocResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<LegalDocResponse>> getAllLegalDocs() {

        List<LegalDocResponse> response = legalDocsService.getAllLegalDocs();

        return ApiResponse.<List<LegalDocResponse>>builder()
                .result(response)
                .build();
    }

    @GetMapping("/investor-organization/{investorOrganizationId}")
    public ApiResponse<List<LegalDocResponse>> getLegalDocsByInvestorOrganization(
            @PathVariable UUID investorOrganizationId) {

        List<LegalDocResponse> response = legalDocsService
                .getLegalDocsByInvestorOrganization(investorOrganizationId);

        return ApiResponse.<List<LegalDocResponse>>builder()
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