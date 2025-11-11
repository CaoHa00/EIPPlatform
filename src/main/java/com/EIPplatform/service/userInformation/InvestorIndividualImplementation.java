package com.EIPplatform.service.userInformation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.InvestorError;
import com.EIPplatform.mapper.businessInformation.InvestorIndividualMapper;
import com.EIPplatform.model.dto.investors.InvestorIndividualCreationRequest;
import com.EIPplatform.model.dto.investors.InvestorIndividualResponse;
import com.EIPplatform.model.dto.investors.InvestorIndividualUpdateRequest;
import com.EIPplatform.model.entity.user.investors.InvestorIndividualDetail;
import com.EIPplatform.repository.user.InvestorIndividualRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvestorIndividualImplementation implements InvestorIndividualInterface {
    
    InvestorIndividualRepository investorIndividualRepository;
    InvestorIndividualMapper investorIndividualMapper;
    ExceptionFactory exceptionFactory;

    @Override
    public InvestorIndividualResponse createInvestorIndividual(InvestorIndividualCreationRequest request) {
        
        if (request.getTaxCode() != null && !request.getTaxCode().isBlank()) {
            if (investorIndividualRepository.existsByTaxCode(request.getTaxCode())) {
                throw exceptionFactory.createAlreadyExistsException(
                    "Investor Individual",
                    "tax code",
                    request.getTaxCode(),
                    InvestorError.TAX_CODE_ALREADY_EXISTS
                );
            }
        }

        InvestorIndividualDetail entity = investorIndividualMapper.toEntity(request);
        InvestorIndividualDetail savedEntity = investorIndividualRepository.save(entity);
        return investorIndividualMapper.toResponse(savedEntity);
    }

    @Override
    public InvestorIndividualResponse updateInvestorIndividual(InvestorIndividualUpdateRequest request) {
        
        InvestorIndividualDetail existingEntity = investorIndividualRepository
                .findById(request.getInvestorId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                    "Investor Individual",
                    request.getInvestorId(),
                    InvestorError.INVESTOR_NOT_FOUND
                ));

        if (request.getTaxCode() != null && !request.getTaxCode().isBlank()) {
            if (investorIndividualRepository.existsByTaxCodeAndInvestorIdNot(
                    request.getTaxCode(),
                    request.getInvestorId())) {
                throw exceptionFactory.createAlreadyExistsException(
                    "Investor Individual",
                    "tax code",
                    request.getTaxCode(),
                    InvestorError.TAX_CODE_ALREADY_EXISTS
                );
            }
        }

        investorIndividualMapper.updateEntityFromRequest(request, existingEntity);
        InvestorIndividualDetail updatedEntity = investorIndividualRepository.save(existingEntity);
        return investorIndividualMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public InvestorIndividualResponse getInvestorIndividualById(UUID investorId) {
        
        InvestorIndividualDetail entity = investorIndividualRepository
                .findById(investorId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                    "Investor Individual",
                    investorId,
                    InvestorError.INVESTOR_NOT_FOUND
                ));

        return investorIndividualMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvestorIndividualResponse> getAllInvestorIndividuals() {
        return investorIndividualRepository.findAll()
                .stream()
                .map(investorIndividualMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInvestorIndividual(UUID investorId) {
        
        if (!investorIndividualRepository.existsById(investorId)) {
            throw exceptionFactory.createNotFoundException(
                "Investor Individual",
                investorId,
                InvestorError.INVESTOR_NOT_FOUND
            );
        }

        investorIndividualRepository.deleteById(investorId);
    }
}