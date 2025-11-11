package com.EIPplatform.service.userInformation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.LegalRepresentativeError;
import com.EIPplatform.mapper.businessInformation.LegalRepresentativeMapper;
import com.EIPplatform.model.dto.legalrepresentative.LegalRepresentativeCreationRequest;
import com.EIPplatform.model.dto.legalrepresentative.LegalRepresentativeResponse;
import com.EIPplatform.model.dto.legalrepresentative.LegalRepresentativeUpdationRequest;
import com.EIPplatform.model.entity.user.legalRepresentative.LegalRepresentative;
import com.EIPplatform.repository.user.LegalRepresentativeRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LegalRepresentativeImplementation implements LegalRepresentativeInterface {
    LegalRepresentativeRepository legalRepresentativeRepository;
    LegalRepresentativeMapper legalRepresentativeMapper;
    ExceptionFactory exceptionFactory;

    @Transactional
    public LegalRepresentativeResponse createLegalRepresentative(LegalRepresentativeCreationRequest request) {

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

        if (request.getTaxCode() != null && !request.getTaxCode().isBlank()) {
            if (legalRepresentativeRepository.existsByTaxCodeAndLegalRepresentativeIdNot(
                    request.getTaxCode(),
                    request.getLegalRepresentativeId())) {
                throw exceptionFactory.createAlreadyExistsException("Legal Representative", "tax code",
                        request.getTaxCode(), LegalRepresentativeError.TAX_CODE_ALREADY_EXISTS);
            }
        }

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

}
