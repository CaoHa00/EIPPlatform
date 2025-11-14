package com.EIPplatform.service.businessInformation;



import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.legalDoc.LegalDocCreationRequest;
import com.EIPplatform.model.dto.legalDoc.LegalDocResponse;
import com.EIPplatform.model.dto.legalDoc.LegalDocUpdateRequest;

public interface LegalDocsInterface {
    LegalDocResponse createLegalDoc(LegalDocCreationRequest request);
    LegalDocResponse updateLegalDoc(LegalDocUpdateRequest request);
    LegalDocResponse getLegalDocById(UUID legalDocId);
    List<LegalDocResponse> getAllLegalDocs();
    List<LegalDocResponse> getLegalDocsByInvestorOrganization(UUID investorOrganizationId);
    void deleteLegalDoc(UUID legalDocId);
}