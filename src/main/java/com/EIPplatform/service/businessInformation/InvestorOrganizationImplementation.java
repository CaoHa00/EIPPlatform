package com.EIPplatform.service.businessInformation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.InvestorError;
import com.EIPplatform.mapper.businessInformation.InvestorOrganizationMapper;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationUpdateRequest;
import com.EIPplatform.model.entity.businessInformation.investors.InvestorOrganizationDetail;
import com.EIPplatform.repository.businessInformation.InvestorOrganizationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvestorOrganizationImplementation implements InvestorOrganizationInterface {
    
    InvestorOrganizationRepository investorOrganizationRepository;
    InvestorOrganizationMapper investorOrganizationMapper;
    ExceptionFactory exceptionFactory;

    @Override
    public InvestorOrganizationResponse createInvestorOrganization(InvestorOrganizationCreationRequest request) {
        
        if (request.getTaxCode() != null && !request.getTaxCode().isBlank()) {
            if (investorOrganizationRepository.existsByTaxCode(request.getTaxCode())) {
                throw exceptionFactory.createAlreadyExistsException(
                    "Investor Organization", 
                    "tax code",
                    request.getTaxCode(), 
                    InvestorError.TAX_CODE_ALREADY_EXISTS
                );
            }
        }

        InvestorOrganizationDetail entity = investorOrganizationMapper.toEntity(request);
        InvestorOrganizationDetail savedEntity = investorOrganizationRepository.save(entity);
        InvestorOrganizationResponse response = investorOrganizationMapper.toResponse(savedEntity);
        return response;
    }

    @Override
    public InvestorOrganizationResponse updateInvestorOrganization(InvestorOrganizationUpdateRequest request) {
        
        InvestorOrganizationDetail existingEntity = investorOrganizationRepository
                .findById(request.getInvestorId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                    "Investor Organization",
                    request.getInvestorId(),
                    InvestorError.INVESTOR_NOT_FOUND
                ));

        if (request.getTaxCode() != null && !request.getTaxCode().isBlank()) {
            if (investorOrganizationRepository.existsByTaxCodeAndInvestorIdNot(
                    request.getTaxCode(),
                    request.getInvestorId())) {
                throw exceptionFactory.createAlreadyExistsException(
                    "Investor Organization",
                    "tax code",
                    request.getTaxCode(),
                    InvestorError.TAX_CODE_ALREADY_EXISTS
                );
            }
        }

        investorOrganizationMapper.updateEntityFromRequest(request, existingEntity);
        InvestorOrganizationDetail updatedEntity = investorOrganizationRepository.save(existingEntity);
        return investorOrganizationMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public InvestorOrganizationResponse getInvestorOrganizationById(UUID investorId) {
        
        InvestorOrganizationDetail entity = investorOrganizationRepository
                .findById(investorId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                    "Investor Organization",
                    investorId,
                    InvestorError.INVESTOR_NOT_FOUND
                ));

        return investorOrganizationMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvestorOrganizationResponse> getAllInvestorOrganizations() {
        return investorOrganizationRepository.findAll()
                .stream()
                .map(investorOrganizationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInvestorOrganization(UUID investorId) {
        
        if (!investorOrganizationRepository.existsById(investorId)) {
            throw exceptionFactory.createNotFoundException(
                "Investor Organization",
                investorId,
                InvestorError.INVESTOR_NOT_FOUND
            );
        }

        investorOrganizationRepository.deleteById(investorId);
    }
}