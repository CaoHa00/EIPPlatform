package com.EIPplatform.service.businessInformation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.LegalRepresentativeError;
import com.EIPplatform.mapper.businessInformation.LegalRepresentativeMapper;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeCreationRequest;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeResponse;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeUpdationRequest;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.legalRepresentative.LegalRepresentative;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.user.LegalRepresentativeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LegalRepresentativeImplementation implements LegalRepresentativeInterface {

    LegalRepresentativeRepository legalRepresentativeRepository;
    LegalRepresentativeMapper legalRepresentativeMapper;
    BusinessDetailRepository businessDetailRepository;
    ExceptionFactory exceptionFactory;

    @Transactional
    public LegalRepresentativeResponse createLegalRepresentative(LegalRepresentativeCreationRequest request) {

        if (request.getBusinessDetailId() == null) {
            throw exceptionFactory.createNotFoundException(
                    "BusinessDetail",
                    request.getBusinessDetailId(),
                    LegalRepresentativeError.BUSINESS_DETAIL_ID_REQUIRED);
        }
        if (!businessDetailRepository.existsById(request.getBusinessDetailId())) {
            throw exceptionFactory.createNotFoundException(
                    "BusinessDetail",
                    "id",
                    request.getBusinessDetailId(),
                    LegalRepresentativeError.BUSINESS_DETAIL_NOT_FOUND);
        }
        if (request.getTaxCode() != null && !request.getTaxCode().isBlank()) {
            if (legalRepresentativeRepository.existsByTaxCode(request.getTaxCode())) {
                throw exceptionFactory.createAlreadyExistsException("Legal Representative", "tax code",
                        request.getTaxCode(), LegalRepresentativeError.TAX_CODE_ALREADY_EXISTS);
            }
        }

        LegalRepresentative entity = legalRepresentativeMapper.toEntity(request);
        LegalRepresentative savedEntity = legalRepresentativeRepository.save(entity);
        return legalRepresentativeMapper.toResponse(savedEntity);
    }

    @Transactional
    public LegalRepresentativeResponse updateLegalRepresentative(LegalRepresentativeUpdationRequest request) {

        LegalRepresentative existingEntity = legalRepresentativeRepository.findById(request.getLegalRepresentativeId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Legal Representative",
                request.getLegalRepresentativeId(), LegalRepresentativeError.LEGAL_REPRESENTATIVE_NOT_FOUND));

        // if (request.getTaxCode() != null && !request.getTaxCode().isBlank()) {
        //     if (legalRepresentativeRepository.existsByTaxCodeAndLegalRepresentativeIdNot(
        //             request.getTaxCode(),
        //             request.getLegalRepresentativeId())) {
        //         throw exceptionFactory.createAlreadyExistsException("Legal Representative", "tax code",
        //                 request.getTaxCode(), LegalRepresentativeError.TAX_CODE_ALREADY_EXISTS);
        //     }
        // }

        legalRepresentativeMapper.updateEntityFromRequest(request, existingEntity);
        LegalRepresentative updatedEntity = legalRepresentativeRepository.save(existingEntity);
        return legalRepresentativeMapper.toResponse(updatedEntity);
    }

    @Transactional(readOnly = true)
    public LegalRepresentativeResponse getLegalRepresentativeById(UUID legalRepresentativeId) {

        LegalRepresentative entity = legalRepresentativeRepository
                .findById(legalRepresentativeId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Legal Representative",
                legalRepresentativeId, LegalRepresentativeError.LEGAL_REPRESENTATIVE_NOT_FOUND));

        return legalRepresentativeMapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<LegalRepresentativeResponse> getAllLegalRepresentatives() {
        return legalRepresentativeRepository.findAll()
                .stream()
                .map(legalRepresentativeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLegalRepresentative(UUID legalRepresentativeId) {

        if (!legalRepresentativeRepository.existsById(legalRepresentativeId)) {
            throw exceptionFactory.createNotFoundException("Legal Representative",
                    legalRepresentativeId, LegalRepresentativeError.LEGAL_REPRESENTATIVE_NOT_FOUND);
        }

        legalRepresentativeRepository.deleteById(legalRepresentativeId);

    }

    @Override
    public LegalRepresentativeResponse getLegalRepresentativeByBusinessDetailId(UUID businessDetailId) {
        BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                "BusinessDetail",
                "id",
                businessDetailId,
                LegalRepresentativeError.BUSINESS_DETAIL_NOT_FOUND));
        LegalRepresentative legalRep = businessDetail.getLegalRepresentative();
        if (legalRep == null) {
            throw exceptionFactory.createNotFoundException(
                    "LegalRepresentative",
                    "businessDetailId",
                    businessDetailId,
                    LegalRepresentativeError.LEGAL_REPRESENTATIVE_NOT_FOUND);
        }

        return legalRepresentativeMapper.toResponse(legalRep);
    }

}
