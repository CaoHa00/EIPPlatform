package com.EIPplatform.service.businessInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualUpdateRequest;

public interface InvestorIndividualInterface {
    InvestorIndividualResponse createInvestorIndividual(InvestorIndividualCreationRequest request);
    InvestorIndividualResponse updateInvestorIndividual(InvestorIndividualUpdateRequest request);
    InvestorIndividualResponse getInvestorIndividualById(UUID investorId);
    List<InvestorIndividualResponse> getAllInvestorIndividuals();
    void deleteInvestorIndividual(UUID investorId);
}