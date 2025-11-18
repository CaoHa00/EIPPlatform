package com.EIPplatform.controller.businessInformation.investor;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationUpdateRequest;
import com.EIPplatform.service.businessInformation.InvestorOrganizationInterface;

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

@RestController
@RequestMapping("/api/v1/investor-organization")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvestorOrganizationController {
    
    InvestorOrganizationInterface investorOrganizationService;

    @PostMapping
    public ApiResponse<InvestorOrganizationResponse> createInvestorOrganization(
            @RequestBody @Valid InvestorOrganizationCreationRequest request) {

        InvestorOrganizationResponse response = investorOrganizationService.createInvestorOrganization(request);

        return ApiResponse.<InvestorOrganizationResponse>builder()
                .message("Investor organization created successfully")
                .result(response)
                .build();
    }

    @PutMapping
    public ApiResponse<InvestorOrganizationResponse> updateInvestorOrganization(
            @RequestBody @Valid InvestorOrganizationUpdateRequest request) {

        InvestorOrganizationResponse response = investorOrganizationService.updateInvestorOrganization(request);

        return ApiResponse.<InvestorOrganizationResponse>builder()
                .message("Investor organization updated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/{investorId}")
    public ApiResponse<InvestorOrganizationResponse> getInvestorOrganizationById(
            @PathVariable UUID investorId) {

        InvestorOrganizationResponse response = investorOrganizationService
                .getInvestorOrganizationById(investorId);

        return ApiResponse.<InvestorOrganizationResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<InvestorOrganizationResponse>> getAllInvestorOrganizations() {

        List<InvestorOrganizationResponse> response = investorOrganizationService.getAllInvestorOrganizations();

        return ApiResponse.<List<InvestorOrganizationResponse>>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{investorId}")
    public ApiResponse<Void> deleteInvestorOrganization(
            @PathVariable UUID investorId) {

        investorOrganizationService.deleteInvestorOrganization(investorId);

        return ApiResponse.<Void>builder()
                .message("Investor organization deleted successfully")
                .build();
    }
}