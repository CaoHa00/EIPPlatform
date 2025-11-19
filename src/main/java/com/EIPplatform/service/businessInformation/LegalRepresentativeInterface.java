package com.EIPplatform.service.businessInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeCreationRequest;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeResponse;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeUpdationRequest;

public interface LegalRepresentativeInterface {
    LegalRepresentativeResponse createLegalRepresentative(LegalRepresentativeCreationRequest request);

    LegalRepresentativeResponse updateLegalRepresentative(LegalRepresentativeUpdationRequest request);

    LegalRepresentativeResponse getLegalRepresentativeById(UUID legalRepresentativeId);

    
    LegalRepresentativeResponse getLegalRepresentativeByBusinessDetailId(UUID businessDetailId);


    List<LegalRepresentativeResponse> getAllLegalRepresentatives();

    void deleteLegalRepresentative(UUID legalRepresentativeId);
}
