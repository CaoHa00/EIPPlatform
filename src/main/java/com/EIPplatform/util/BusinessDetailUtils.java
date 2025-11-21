package com.EIPplatform.util;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.BusinessDetailError;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailUpdateDTO;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.enums.OperationType;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BusinessDetailUtils {

    private final BusinessDetailRepository businessDetailRepository;
    private final ExceptionFactory exceptionFactory;

    public void validateOperationDetails(OperationType operationType, String seasonalDescription) {
        if (OperationType.SEASONAL.equals(operationType)) {
            if (seasonalDescription == null || seasonalDescription.trim().isEmpty()) {
                throw new IllegalArgumentException("Seasonal must provide description of operating hours");
            }
        }
    }

    public void validateUniqueFields(BusinessDetailDTO dto, UUID currentId) {
        // Validate taxCode
        Optional<BusinessDetail> existingTax = businessDetailRepository.findByTaxCode(dto.getTaxCode());
        if (existingTax.isPresent() && (currentId == null || !existingTax.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "taxCode", dto.getTaxCode(), BusinessDetailError.TAX_CODE_DUPLICATE
            );
        }

        // Validate businessRegistrationNumber
        Optional<BusinessDetail> existingRegNum = businessDetailRepository.findByBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        if (existingRegNum.isPresent() && (currentId == null || !existingRegNum.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "businessRegistrationNumber", dto.getBusinessRegistrationNumber(), BusinessDetailError.BUSINESS_REGISTRATION_NUMBER_DUPLICATE
            );
        }

        // Validate phoneNumber
        Optional<BusinessDetail> existingPhone = businessDetailRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (existingPhone.isPresent() && (currentId == null || !existingPhone.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "phoneNumber", dto.getPhoneNumber(), BusinessDetailError.PHONE_NUMBER_DUPLICATE
            );
        }
    }

    public void validateUniqueFields(BusinessDetailUpdateDTO dto, UUID currentId) {
        // Validate taxCode
        Optional<BusinessDetail> existingTax = businessDetailRepository.findByTaxCode(dto.getTaxCode());
        if (existingTax.isPresent() && (currentId == null || !existingTax.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "taxCode", dto.getTaxCode(), BusinessDetailError.TAX_CODE_DUPLICATE
            );
        }

        // Validate businessRegistrationNumber
        Optional<BusinessDetail> existingRegNum = businessDetailRepository.findByBusinessRegistrationNumber(dto.getBusinessRegistrationNumber());
        if (existingRegNum.isPresent() && (currentId == null || !existingRegNum.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "businessRegistrationNumber", dto.getBusinessRegistrationNumber(), BusinessDetailError.BUSINESS_REGISTRATION_NUMBER_DUPLICATE
            );
        }

        // Validate phoneNumber
        Optional<BusinessDetail> existingPhone = businessDetailRepository.findByPhoneNumber(dto.getPhoneNumber());
        if (existingPhone.isPresent() && (currentId == null || !existingPhone.get().getBusinessDetailId().equals(currentId))) {
            throw exceptionFactory.createAlreadyExistsException(
                    "BusinessDetail", "phoneNumber", dto.getPhoneNumber(), BusinessDetailError.PHONE_NUMBER_DUPLICATE
            );
        }
    }
}