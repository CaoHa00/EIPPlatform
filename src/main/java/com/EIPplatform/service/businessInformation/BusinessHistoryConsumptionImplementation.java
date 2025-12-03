package com.EIPplatform.service.businessInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.BusinessDetailError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.mapper.businessInformation.BusinessHistoryConsumptionMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionCreateDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionUpdateDTO;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.BusinessHistoryConsumption;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.businessInformation.BusinessHistoryConsumptionRepository;

import com.EIPplatform.utils.StringNormalizerUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BusinessHistoryConsumptionImplementation implements BusinessHistoryConsumptionInterface {

    BusinessHistoryConsumptionRepository businessHistoryConsumptionRepository;
    BusinessDetailRepository businessDetailRepository;
    UserAccountRepository userAccountRepository;
    BusinessHistoryConsumptionMapper businessHistoryConsumptionMapper;
    ExceptionFactory exceptionFactory;

    @Override
    public List<BusinessHistoryConsumptionDTO> findByBusinessDetailId(UUID businessDetailId, UUID userAccountId) {
        userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        userAccountId,
                        UserError.NOT_FOUND
                ));
        BusinessDetail businessDetail = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND
                ));
        if (!businessDetail.getBusinessDetailId().equals(businessDetailId)) {
            throw exceptionFactory.createCustomException(
                    "BusinessHistoryConsumption",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, businessDetailId),
                    UserError.UNAUTHORIZED_ACCESS
            );
        }
        return businessHistoryConsumptionMapper.toDTOList(
                businessHistoryConsumptionRepository.findByBusinessDetail_BusinessDetailId(businessDetailId)
        );
    }

    @Override
    @Transactional
    public BusinessHistoryConsumptionDTO createBusinessHistoryConsumption(
            UUID userAccountId,
            BusinessHistoryConsumptionCreateDTO dto) {
        dto = StringNormalizerUtil.normalizeRequest(dto);
        BusinessDetail businessDetail = businessDetailRepository
                .findByUserAccounts_UserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND
                ));

        UUID dtoBusinessDetailId = dto.getBusinessDetailId();

        if (!businessDetail.getBusinessDetailId().equals(dtoBusinessDetailId)) {
            throw exceptionFactory.createCustomException(
                    "BusinessHistoryConsumption",
                    List.of("userAccountId", "businessDetailId"),
                    List.of(userAccountId, dtoBusinessDetailId),
                    UserError.UNAUTHORIZED_ACCESS
            );
        }

        BusinessHistoryConsumption entity = businessHistoryConsumptionMapper.toEntity(dto);
        entity.setBusinessDetail(businessDetail);
        entity = businessHistoryConsumptionRepository.save(entity);

        BusinessHistoryConsumptionDTO response = businessHistoryConsumptionMapper.toDTO(entity);

        log.info("Created BusinessHistoryConsumption - ID: {} for BusinessDetail: {}",
                entity.getBusinessHistoryConsumptionId(), businessDetail.getBusinessDetailId());

        return response;
    }

    @Override
    public BusinessHistoryConsumptionDTO updateBusinessHistoryConsumption(UUID userAccountId, UUID businessHistoryConsumptionId, BusinessHistoryConsumptionUpdateDTO dto) {
        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "UserAccount",
                        userAccountId,
                        UserError.NOT_FOUND
                ));
        BusinessDetail businessDetail = businessDetailRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessDetail",
                        userAccountId,
                        BusinessDetailError.NOT_FOUND
                ));
        BusinessHistoryConsumption entity = businessHistoryConsumptionRepository.findById(businessHistoryConsumptionId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "BusinessHistoryConsumption",
                        businessHistoryConsumptionId,
                        BusinessDetailError.NOT_FOUND
                ));
        if (!entity.getBusinessDetail().getBusinessDetailId().equals(businessDetail.getBusinessDetailId())) {
            throw exceptionFactory.createCustomException(
                    "BusinessHistoryConsumption",
                    List.of("userAccountId", "businessHistoryConsumptionId"),
                    List.of(userAccountId, businessHistoryConsumptionId),
                    UserError.UNAUTHORIZED_ACCESS
            );
        }
        businessHistoryConsumptionMapper.updateEntityFromDto(dto, entity);
        entity = businessHistoryConsumptionRepository.save(entity);
        BusinessHistoryConsumptionDTO response = businessHistoryConsumptionMapper.toDTO(entity);
        log.info("Updated BusinessHistoryConsumption - ID: {} for BusinessDetail: {}",
                entity.getBusinessHistoryConsumptionId(), businessDetail.getBusinessDetailId());
        return response;
    }

//    @Override
//    public void deleteByBusinessDetailId(UUID businessDetailId, UUID userAccountId) {
//        UserAccount userAccount = userAccountRepository.findByUserAccountId(userAccountId)
//                .orElseThrow(() -> exceptionFactory.createNotFoundException(
//                        "UserAccount",
//                        "userAccountId",
//                        userAccountId,
//                        UserError.NOT_FOUND
//                ));
//        BusinessDetail businessDetail = businessDetailRepository.findByUserAccountId(userAccountId)
//                .orElseThrow(() -> exceptionFactory.createNotFoundException(
//                        "BusinessDetail",
//                        "userAccountId",
//                        userAccountId,
//                        BusinessDetailError.NOT_FOUND
//                ));
//        if (!businessDetail.getBusinessDetailId().equals(businessDetailId)) {
//            throw exceptionFactory.createCustomException(
//                    "BusinessHistoryConsumption",
//                    List.of("userAccountId", "businessDetailId"),
//                    List.of(userAccountId, businessDetailId),
//                    UserError.UNAUTHORIZED_ACCESS
//            );
//        }
//        businessHistoryConsumptionRepository.deleteByBusinessDetail_BusinessDetailId(businessDetailId);
//        log.info("Deleted all BusinessHistoryConsumption for BusinessDetail: {} by UserAccount: {}",
//                businessDetailId, userAccountId);
//    }
}