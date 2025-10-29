package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;

public interface  BusinessDetailInterface {
    BusinessDetailDTO findById(UUID id);
    void deleteById(UUID id);
    BusinessDetailDTO create(BusinessDetailDTO dto);
    BusinessDetailDTO update(UUID id, BusinessDetailDTO dto);
    List<BusinessDetailDTO> findAll();
    BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID id);
}
