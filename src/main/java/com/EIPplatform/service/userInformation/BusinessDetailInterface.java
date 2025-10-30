package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;

public interface BusinessDetailInterface {
    BusinessDetailResponse findByBusinessDetailId(UUID id);
    void deleteByBusinessDetailId(UUID id);
    BusinessDetailResponse createBusinessDetail(UUID userAccountId, BusinessDetailDTO dto);
    BusinessDetailResponse updateBusinessDetail(UUID id, BusinessDetailDTO dto);
    List<BusinessDetailResponse> findAll();
    BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID id);
}