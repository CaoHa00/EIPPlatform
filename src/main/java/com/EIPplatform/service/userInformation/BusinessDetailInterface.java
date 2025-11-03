package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;

public interface BusinessDetailInterface {
    BusinessDetailResponse findByUserAccountId(UUID id);
    void deleteByBusinessDetailId(UUID id);
    BusinessDetailResponse createBusinessDetail(UUID userAccountId, BusinessDetailDTO dto, MultipartFile isoFile);
    BusinessDetailResponse updateBusinessDetail(UUID id, BusinessDetailDTO dto, MultipartFile isoFile);
    List<BusinessDetailResponse> findAll();
    BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID id);

    // File-specific methods
    BusinessDetailResponse uploadIsoCertificateFile(UUID businessDetailId, MultipartFile file);
    void deleteIsoCertificateFile(UUID businessDetailId);
    boolean hasIsoCertificateFile(UUID businessDetailId);
}