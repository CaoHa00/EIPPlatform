package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.util.BusinessDetailUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.businessInformation.BusinessDetailMapper;
import com.EIPplatform.mapper.businessInformation.BusinessDetailWithHistoryConsumptionMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;
import com.EIPplatform.repository.user.BusinessDetailRepository;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessDetailImplementation implements BusinessDetailInterface {
    BusinessDetailRepository businessDetailRepository;
    BusinessDetailMapper businessDetailMapper;
    BusinessDetailWithHistoryConsumptionMapper businessDetailWithHistoryConsumptionMapper;
    UserAccountRepository userAccountRepository;
    ExceptionFactory exceptionFactory;
    BusinessDetailUtils businessDetailUtils;

    @Override
    public BusinessDetailResponse findByBusinessDetailId(UUID businessDetailId) {
        return businessDetailRepository.findById(businessDetailId)
                .map(businessDetailMapper::toResponse)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "businessDetailId",
                        businessDetailId,
                        UserError.NOT_FOUND
                ));
    }

    @Override
    public void deleteByBusinessDetailId(UUID businessDetailId) {
        if (!businessDetailRepository.existsById(businessDetailId)) {
            throw exceptionFactory.createNotFoundException(
                    "BusinessDetail",
                    "businessDetailId",
                    businessDetailId,
                    UserError.NOT_FOUND
            );
        }
        businessDetailRepository.deleteById(businessDetailId);
    }

    @Override
    public BusinessDetailResponse createBusinessDetail(UUID userAccountId, BusinessDetailDTO dto) {
        businessDetailUtils.validateOperationDetails(dto.getOperationType(), dto.getSeasonalDescription());

        businessDetailUtils.validateUniqueFields(dto, null);

        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        "userAccountId",
                        userAccountId,
                        UserError.NOT_FOUND
                ));

        BusinessDetail entity = businessDetailMapper.toEntity(dto);

        entity.getUserAccounts().add(userAccount);
        userAccount.setBusinessDetail(entity);

        entity = businessDetailRepository.saveAndFlush(entity);

        if (entity.getBusinessDetailId() == null) {
            throw exceptionFactory.createCustomException(
                    "BusinessDetail",
                    List.of("operation", "companyName"),
                    List.of("save", dto.getCompanyName()),
                    UserError.ID_GENERATION_FAILED
            );
        }

        userAccountRepository.flush();

        BusinessDetailResponse response = businessDetailMapper.toResponse(entity);

        log.info("Created BusinessDetail - ID: {}, Company: {}, TaxCode: {}",
                entity.getBusinessDetailId(), entity.getCompanyName(), entity.getTaxCode());

        return response;
    }

    @Override
    public BusinessDetailResponse updateBusinessDetail(UUID id, BusinessDetailDTO dto) {
        BusinessDetail entity = businessDetailRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "id",
                        id,
                        UserError.NOT_FOUND
                ));

        businessDetailUtils.validateOperationDetails(dto.getOperationType(), dto.getSeasonalDescription());

        businessDetailUtils.validateUniqueFields(dto, id);

        entity.setCompanyName(dto.getCompanyName());
        entity.setLegalRepresentative(dto.getLegalPresentative());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setLocation(dto.getLocation());
        entity.setIndustrySector(dto.getIndustrySector());
        entity.setScaleCapacity(dto.getScaleCapacity());
        entity.setISO_certificate_14001(dto.getISO_certificate_14001());
        entity.setBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        entity.setTaxCode(dto.getTaxCode());
        entity.setOperationType(dto.getOperationType());
        entity.setSeasonalDescription(dto.getSeasonalDescription());

        entity = businessDetailRepository.save(entity);

        BusinessDetailResponse response = businessDetailMapper.toResponse(entity);

        log.info("Updated BusinessDetail - ID: {}, Company: {}, TaxCode: {}",
                id, entity.getCompanyName(), entity.getTaxCode());

        return response;
    }

    @Override
    public List<BusinessDetailResponse> findAll() {
        return businessDetailMapper.toResponseList(businessDetailRepository.findAll());
    }

    @Override
    public BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID id) {
        var entity = businessDetailRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        "id",
                        id,
                        UserError.NOT_FOUND
                ));
        return businessDetailWithHistoryConsumptionMapper.toDTO(entity);
    }
}