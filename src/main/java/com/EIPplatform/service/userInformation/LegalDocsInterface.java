package com.EIPplatform.service.userInformation;



import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.legalDocs.LegalDocsCreationRequest;
import com.EIPplatform.model.dto.legalDocs.LegalDocsResponse;
import com.EIPplatform.model.dto.legalDocs.LegalDocsUpdateRequest;

public interface LegalDocsInterface {
    LegalDocsResponse createLegalDoc(LegalDocsCreationRequest request);
    LegalDocsResponse updateLegalDoc(LegalDocsUpdateRequest request);
    LegalDocsResponse getLegalDocById(UUID legalDocId);
    List<LegalDocsResponse> getAllLegalDocs();
    List<LegalDocsResponse> getLegalDocsByInvestorOrganization(UUID investorOrganizationId);
    void deleteLegalDoc(UUID legalDocId);
}