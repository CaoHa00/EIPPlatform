package com.EIPplatform.controller.businessInformation;


import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualUpdateRequest;
import com.EIPplatform.service.businessInformation.InvestorIndividualInterface;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/investor-individual")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvestorIndividualController {
    
    InvestorIndividualInterface investorIndividualService;

    @PostMapping
    public ApiResponse<InvestorIndividualResponse> createInvestorIndividual(
            @RequestBody @Valid InvestorIndividualCreationRequest request) {

        InvestorIndividualResponse response = investorIndividualService.createInvestorIndividual(request);

        return ApiResponse.<InvestorIndividualResponse>builder()
                .message("Investor individual created successfully")
                .result(response)
                .build();
    }

    @PutMapping
    public ApiResponse<InvestorIndividualResponse> updateInvestorIndividual(
            @RequestBody @Valid InvestorIndividualUpdateRequest request) {

        InvestorIndividualResponse response = investorIndividualService.updateInvestorIndividual(request);

        return ApiResponse.<InvestorIndividualResponse>builder()
                .message("Investor individual updated successfully")
                .result(response)
                .build();
    }

    @GetMapping("/{investorId}")
    public ApiResponse<InvestorIndividualResponse> getInvestorIndividualById(
            @PathVariable UUID investorId) {

        InvestorIndividualResponse response = investorIndividualService
                .getInvestorIndividualById(investorId);

        return ApiResponse.<InvestorIndividualResponse>builder()
                .result(response)
                .build();
    }

    @GetMapping
    public ApiResponse<List<InvestorIndividualResponse>> getAllInvestorIndividuals() {

        List<InvestorIndividualResponse> response = investorIndividualService.getAllInvestorIndividuals();

        return ApiResponse.<List<InvestorIndividualResponse>>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{investorId}")
    public ApiResponse<Void> deleteInvestorIndividual(
            @PathVariable UUID investorId) {

        investorIndividualService.deleteInvestorIndividual(investorId);

        return ApiResponse.<Void>builder()
                .message("Investor individual deleted successfully")
                .build();
    }
}