package com.EIPplatform.service.businessInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;

public interface BusinessDetailInterface {
    BusinessDetailResponse findByUserAccountId(UUID userAccountId);
    void deleteByUserAccountId(UUID userAccountId);
    BusinessDetailResponse createBusinessDetail(UUID userAccountId, BusinessDetailDTO dto, MultipartFile isoFile);
    BusinessDetailResponse updateBusinessDetail(UUID userAccountId, BusinessDetailUpdateDTO dto, MultipartFile isoFile);
    UUID findByBusinessDetailId(UUID userAccountId);
    List<BusinessDetailResponse> findAll();
//    BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID businessDetailId);

    // File-specific methods
    BusinessDetailResponse uploadIsoCertificateFile(UUID userAccountId, MultipartFile file);
    void deleteIsoCertificateFile(UUID userAccountId);
    boolean hasIsoCertificateFile(UUID userAccountId);
}