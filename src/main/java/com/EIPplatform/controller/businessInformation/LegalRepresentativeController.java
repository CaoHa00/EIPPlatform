package com.EIPplatform.controller.businessInformation;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeCreationRequest;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeResponse;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeUpdationRequest;
import com.EIPplatform.service.businessInformation.LegalRepresentativeInterface;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/legal-representative")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LegalRepresentativeController {
    private final LegalRepresentativeInterface legalRepresentativeService;

    @PostMapping
    public ApiResponse<LegalRepresentativeResponse> createLegalRepresentative(
            @RequestBody @Valid LegalRepresentativeCreationRequest request) {

        LegalRepresentativeResponse response = legalRepresentativeService.createLegalRepresentative(request);

        return ApiResponse.<LegalRepresentativeResponse>builder()
                .message("Legal representative created successfully")
                .result(response)
                .build();
    }

    @PutMapping
    public ApiResponse<LegalRepresentativeResponse> updateLegalRepresentative(
            @RequestBody @Valid LegalRepresentativeUpdationRequest request) {

        LegalRepresentativeResponse response = legalRepresentativeService.updateLegalRepresentative(request);

        return ApiResponse.<LegalRepresentativeResponse>builder()
                .message("Legal representative updated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/{legalRepresentativeId}")
    public ApiResponse<LegalRepresentativeResponse> getLegalRepresentativeById(
            @PathVariable UUID legalRepresentativeId) {

        LegalRepresentativeResponse response = legalRepresentativeService
                .getLegalRepresentativeById(legalRepresentativeId);

        return ApiResponse.<LegalRepresentativeResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping("/by-business/{businessDetailId}")
    public ApiResponse<LegalRepresentativeResponse> getLegalRepresentativeByBusinessDetailId(
            @PathVariable UUID businessDetailId) {

        LegalRepresentativeResponse response = legalRepresentativeService
                .getLegalRepresentativeByBusinessDetailId(businessDetailId);

        return ApiResponse.<LegalRepresentativeResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<LegalRepresentativeResponse>> getAllLegalRepresentatives() {

        List<LegalRepresentativeResponse> response = legalRepresentativeService.getAllLegalRepresentatives();

        return ApiResponse.<List<LegalRepresentativeResponse>>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{legalRepresentativeId}")
    public ApiResponse<Void> deleteLegalRepresentative(
            @PathVariable UUID legalRepresentativeId) {

        legalRepresentativeService.deleteLegalRepresentative(legalRepresentativeId);

        return ApiResponse.<Void>builder()
                .message("Legal representative deleted successfully")
                .build();
    }
}

