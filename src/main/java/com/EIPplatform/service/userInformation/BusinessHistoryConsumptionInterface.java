package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;

public interface BusinessHistoryConsumptionInterface {
    
    List<BusinessHistoryConsumptionDTO> findByBusinessDetailId(UUID businessDetailId);

    BusinessHistoryConsumptionDTO create(BusinessHistoryConsumptionDTO dto);

    void deleteByBusinessDetailId(UUID businessDetailId);
}
