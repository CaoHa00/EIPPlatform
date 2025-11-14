package com.EIPplatform.service.businessInformation;



import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationUpdateRequest;

public interface InvestorOrganizationInterface {
    InvestorOrganizationResponse createInvestorOrganization(InvestorOrganizationCreationRequest request);
    InvestorOrganizationResponse updateInvestorOrganization(InvestorOrganizationUpdateRequest request);
    InvestorOrganizationResponse getInvestorOrganizationById(UUID investorId);
    List<InvestorOrganizationResponse> getAllInvestorOrganizations();
    void deleteInvestorOrganization(UUID investorId);
}