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

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BusinessDetailUtils {

    private final BusinessDetailRepository businessDetailRepository;
    private final ExceptionFactory exceptionFactory;

    public void validateUniqueFieldsForCreate(BusinessDetailDTO dto) {
        validateTaxCode(dto.getTaxCode(), null);
        validateBusinessRegistrationNumber(dto.getBusinessRegistrationNumber(), null);
        validatePhoneNumber(dto.getPhoneNumber(), null);
        validateFacilityName(dto.getFacilityName(), null);
    }

    public void validateUniqueFieldsForUpdate(BusinessDetailUpdateDTO dto, UUID currentId) {
        if (dto.getTaxCode() != null && !dto.getTaxCode().isBlank()) {
            validateTaxCode(dto.getTaxCode().trim(), currentId);
        }
        if (dto.getBusinessRegistrationNumber() != null && !dto.getBusinessRegistrationNumber().isBlank()) {
            validateBusinessRegistrationNumber(dto.getBusinessRegistrationNumber().trim(), currentId);
        }
        if (dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            validatePhoneNumber(dto.getPhoneNumber().trim(), currentId);
        }
        if (dto.getFacilityName() != null && !dto.getFacilityName().isBlank()) {
            validateFacilityName(dto.getFacilityName().trim(), currentId);
        }
    }

    private void validateTaxCode(String taxCode, UUID currentId) {
        if (taxCode == null || taxCode.isBlank()) return;

        businessDetailRepository.findByTaxCode(taxCode.trim())
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getBusinessDetailId().equals(currentId)) {
                        throw exceptionFactory.createAlreadyExistsException(
                                "BusinessDetail", "businessRegistrationNumber",taxCode,
                                BusinessDetailError.BUSINESS_REGISTRATION_NUMBER_DUPLICATE
                        );
                    }
                });
    }

    private void validateBusinessRegistrationNumber(String brn, UUID currentId) {
        if (brn == null || brn.isBlank()) return;

        businessDetailRepository.findByBusinessRegistrationNumber(brn.trim())
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getBusinessDetailId().equals(currentId)) {
                        throw exceptionFactory.createAlreadyExistsException(
                                "BusinessDetail", "businessRegistrationNumber", brn.trim(),
                                BusinessDetailError.BUSINESS_REGISTRATION_NUMBER_DUPLICATE
                        );
                    }
                });
    }

    private void validatePhoneNumber(String phone, UUID currentId) {
        if (phone == null || phone.isBlank()) return;

        businessDetailRepository.findByPhoneNumber(phone.trim())
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getBusinessDetailId().equals(currentId)) {
                        throw exceptionFactory.createAlreadyExistsException(
                                "BusinessDetail", "phoneNumber", phone.trim(),
                                BusinessDetailError.PHONE_NUMBER_DUPLICATE
                        );
                    }
                });
    }

    private void validateFacilityName(String name, UUID currentId) {
        if (name == null || name.isBlank()) return;

        String trimmedName = name.trim();
        businessDetailRepository.findByFacilityName(trimmedName)
                .ifPresent(existing -> {
                    if (currentId == null || !existing.getBusinessDetailId().equals(currentId)) {
                        throw exceptionFactory.createAlreadyExistsException(
                                "BusinessDetail", "facilityName", trimmedName,
                                BusinessDetailError.FACILITY_NAME_DUPLICATE
                        );
                    }
                });
    }

    public void validateOperationDetails(OperationType operationType, String seasonalDescription) {
        if (OperationType.SEASONAL.equals(operationType)) {
            if (seasonalDescription == null || seasonalDescription.trim().isEmpty()) {
                throw new IllegalArgumentException("Seasonal operation must provide description of operating hours");
            }
        }
    }
}