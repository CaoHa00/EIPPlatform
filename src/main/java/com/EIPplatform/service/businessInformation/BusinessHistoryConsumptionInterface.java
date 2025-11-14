package com.EIPplatform.service.businessInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionCreateDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionUpdateDTO;

public interface BusinessHistoryConsumptionInterface {

    List<BusinessHistoryConsumptionDTO> findByBusinessDetailId(UUID businessDetailId, UUID userAccountId);

    BusinessHistoryConsumptionDTO createBusinessHistoryConsumption(UUID userAccountId, BusinessHistoryConsumptionCreateDTO dto);

    BusinessHistoryConsumptionDTO updateBusinessHistoryConsumption(UUID userAccountId, UUID businessHistoryConsumptionId, BusinessHistoryConsumptionUpdateDTO dto);

//    void deleteByBusinessDetailId(UUID businessDetailId, UUID userAccountId);
}