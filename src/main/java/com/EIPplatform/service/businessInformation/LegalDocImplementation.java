package com.EIPplatform.service.businessInformation;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.LegalDocError;
import com.EIPplatform.mapper.businessInformation.LegalDocMapper;
import com.EIPplatform.model.dto.legalDoc.LegalDocCreationRequest;
import com.EIPplatform.model.dto.legalDoc.LegalDocResponse;
import com.EIPplatform.model.dto.legalDoc.LegalDocUpdateRequest;
import com.EIPplatform.model.entity.businessInformation.investors.InvestorOrganizationDetail;
import com.EIPplatform.model.entity.legalDoc.LegalDoc;
import com.EIPplatform.repository.businessInformation.InvestorOrganizationRepository;
import com.EIPplatform.repository.user.LegalDocRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LegalDocImplementation implements LegalDocsInterface {

    LegalDocRepository legalDocsRepository;
    InvestorOrganizationRepository investorOrganizationRepository;
    LegalDocMapper legalDocsMapper;
    ExceptionFactory exceptionFactory;

    @Override
    @Transactional
    public LegalDocResponse createLegalDoc(LegalDocCreationRequest request) {

        validateGPMTProjectName(request.getType(), request.getProjectName());

        InvestorOrganizationDetail investorOrganization = investorOrganizationRepository
                .findById(request.getInvestorOrganizationId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Investor Organization",
                        request.getInvestorOrganizationId(),
                        LegalDocError.INVESTOR_ORGANIZATION_NOT_FOUND
                ));

        if (legalDocsRepository.existsByNumberAndInvestorOrganization_InvestorId(
                request.getNumber(),
                request.getInvestorOrganizationId())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "Legal Document",
                    "number",
                    request.getNumber(),
                    LegalDocError.LEGAL_DOC_NUMBER_ALREADY_EXISTS
            );
        }

        LegalDoc entity = legalDocsMapper.toEntity(request);
        entity.setInvestorOrganization(investorOrganization);
        
        LegalDoc savedEntity = legalDocsRepository.save(entity);
        return legalDocsMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public LegalDocResponse updateLegalDoc(LegalDocUpdateRequest request) {

        validateGPMTProjectName(request.getType(), request.getProjectName());

        LegalDoc existingEntity = legalDocsRepository
                .findById(request.getLegalDocId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Legal Document",
                        request.getLegalDocId(),
                        LegalDocError.LEGAL_DOC_NOT_FOUND
                ));

        InvestorOrganizationDetail investorOrganization = investorOrganizationRepository
                .findById(request.getInvestorOrganizationId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Investor Organization",
                        request.getInvestorOrganizationId(),
                        LegalDocError.INVESTOR_ORGANIZATION_NOT_FOUND
                ));

        if (legalDocsRepository.existsByNumberAndInvestorOrganization_InvestorIdAndLegalDocIdNot(
                request.getNumber(),
                request.getInvestorOrganizationId(),
                request.getLegalDocId())) {
            throw exceptionFactory.createAlreadyExistsException(
                    "Legal Document",
                    "number",
                    request.getNumber(),
                    LegalDocError.LEGAL_DOC_NUMBER_ALREADY_EXISTS
            );
        }

        legalDocsMapper.updateEntityFromRequest(request, existingEntity);
        existingEntity.setInvestorOrganization(investorOrganization);
        
        LegalDoc updatedEntity = legalDocsRepository.save(existingEntity);
        return legalDocsMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public LegalDocResponse getLegalDocById(UUID legalDocId) {

        LegalDoc entity = legalDocsRepository
                .findById(legalDocId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Legal Document",
                        legalDocId,
                        LegalDocError.LEGAL_DOC_NOT_FOUND
                ));

        return legalDocsMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalDocResponse> getAllLegalDocs() {
        return legalDocsRepository.findAll()
                .stream()
                .map(legalDocsMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LegalDocResponse> getLegalDocsByInvestorOrganization(UUID investorOrganizationId) {

        if (!investorOrganizationRepository.existsById(investorOrganizationId)) {
            throw exceptionFactory.createNotFoundException(
                    "Investor Organization",
                    investorOrganizationId,
                    LegalDocError.INVESTOR_ORGANIZATION_NOT_FOUND
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
                    LegalDocError.LEGAL_DOC_NOT_FOUND
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
                    LegalDocError.PROJECT_NAME_REQUIRED_FOR_GPMT
            );
        }
    }
}