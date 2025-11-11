package com.EIPplatform.service.userInformation;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.LegalDocsError;
import com.EIPplatform.mapper.businessInformation.LegalDocsMapper;
import com.EIPplatform.model.dto.legalDocs.LegalDocsCreationRequest;
import com.EIPplatform.model.dto.legalDocs.LegalDocsResponse;
import com.EIPplatform.model.dto.legalDocs.LegalDocsUpdateRequest;
import com.EIPplatform.model.entity.user.investors.InvestorOrganizationDetail;
import com.EIPplatform.model.entity.user.legalDocs.LegalDocs;
import com.EIPplatform.repository.user.InvestorOrganizationRepository;
import com.EIPplatform.repository.user.LegalDocsRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LegalDocsImplementation implements LegalDocsInterface {

    LegalDocsRepository legalDocsRepository;
    InvestorOrganizationRepository investorOrganizationRepository;
    LegalDocsMapper legalDocsMapper;
    ExceptionFactory exceptionFactory;

    @Override
    @Transactional
    public LegalDocsResponse createLegalDoc(LegalDocsCreationRequest request) {

        validateGPMTProjectName(request.getType(), request.getProjectName());

        InvestorOrganizationDetail investorOrganization = investorOrganizationRepository
                .findById(request.getInvestorOrganizationId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Investor Organization",
                        request.getInvestorOrganizationId(),
                        LegalDocsError.INVESTOR_ORGANIZATION_NOT_FOUND
                ));

        if (legalDocsRepository.existsByNumberAndInvestorOrganization_InvestorId(
                request.getNumber(),
                request.getInvestorOrganizationId())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "Legal Document",
                    "number",
                    request.getNumber(),
                    LegalDocsError.LEGAL_DOC_NUMBER_ALREADY_EXISTS
            );
        }

        LegalDocs entity = legalDocsMapper.toEntity(request);
        entity.setInvestorOrganization(investorOrganization);
        
        LegalDocs savedEntity = legalDocsRepository.save(entity);
        return legalDocsMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public LegalDocsResponse updateLegalDoc(LegalDocsUpdateRequest request) {

        validateGPMTProjectName(request.getType(), request.getProjectName());

        LegalDocs existingEntity = legalDocsRepository
                .findById(request.getLegalDocId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Legal Document",
                        request.getLegalDocId(),
                        LegalDocsError.LEGAL_DOC_NOT_FOUND
                ));

        InvestorOrganizationDetail investorOrganization = investorOrganizationRepository
                .findById(request.getInvestorOrganizationId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Investor Organization",
                        request.getInvestorOrganizationId(),
                        LegalDocsError.INVESTOR_ORGANIZATION_NOT_FOUND
                ));

        if (legalDocsRepository.existsByNumberAndInvestorOrganization_InvestorIdAndLegalDocIdNot(
                request.getNumber(),
                request.getInvestorOrganizationId(),
                request.getLegalDocId())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "Legal Document",
                    "number",
                    request.getNumber(),
                    LegalDocsError.LEGAL_DOC_NUMBER_ALREADY_EXISTS
            );
        }

        legalDocsMapper.updateEntityFromRequest(request, existingEntity);
        existingEntity.setInvestorOrganization(investorOrganization);
        
        LegalDocs updatedEntity = legalDocsRepository.save(existingEntity);
        return legalDocsMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public LegalDocsResponse getLegalDocById(UUID legalDocId) {

        LegalDocs entity = legalDocsRepository
                .findById(legalDocId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Legal Document",
                        legalDocId,
                        LegalDocsError.LEGAL_DOC_NOT_FOUND
                ));

        return legalDocsMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalDocsResponse> getAllLegalDocs() {
        return legalDocsRepository.findAll()
                .stream()
                .map(legalDocsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalDocsResponse> getLegalDocsByInvestorOrganization(UUID investorOrganizationId) {

        if (!investorOrganizationRepository.existsById(investorOrganizationId)) {
            throw exceptionFactory.createNotFoundException(
                    "Investor Organization",
                    investorOrganizationId,
                    LegalDocsError.INVESTOR_ORGANIZATION_NOT_FOUND
            );
        }

        return legalDocsRepository.findByInvestorOrganization_InvestorId(investorOrganizationId)
                .stream()
                .map(legalDocsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteLegalDoc(UUID legalDocId) {

        if (!legalDocsRepository.existsById(legalDocId)) {
            throw exceptionFactory.createNotFoundException(
                    "Legal Document",
                    legalDocId,
                    LegalDocsError.LEGAL_DOC_NOT_FOUND
            );
        }

        legalDocsRepository.deleteById(legalDocId);
    }

    private void validateGPMTProjectName(String type, String projectName) {
        if ("GPMT".equalsIgnoreCase(type) && (projectName == null || projectName.isBlank())) {
            throw exceptionFactory.createValidationException(
                    "Legal Document",
                    "projectName",
                    null,
                    LegalDocsError.PROJECT_NAME_REQUIRED_FOR_GPMT
            );
        }
    }
}