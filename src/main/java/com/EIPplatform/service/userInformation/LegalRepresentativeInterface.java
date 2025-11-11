package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.legalrepresentative.LegalRepresentativeCreationRequest;
import com.EIPplatform.model.dto.legalrepresentative.LegalRepresentativeResponse;
import com.EIPplatform.model.dto.legalrepresentative.LegalRepresentativeUpdationRequest;

public interface LegalRepresentativeInterface {
    LegalRepresentativeResponse createLegalRepresentative(LegalRepresentativeCreationRequest request);

    LegalRepresentativeResponse updateLegalRepresentative(LegalRepresentativeUpdationRequest request);

    LegalRepresentativeResponse getLegalRepresentativeById(UUID legalRepresentativeId);

    List<LegalRepresentativeResponse> getAllLegalRepresentatives();

    void deleteLegalRepresentative(UUID legalRepresentativeId);
}
