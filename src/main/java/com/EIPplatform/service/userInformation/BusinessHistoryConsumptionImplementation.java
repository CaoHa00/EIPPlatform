package com.EIPplatform.service.userInformation;


import com.EIPplatform.mapper.businessInformation.BusinessDetailHistoryConsumptionMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.user.businessInformation.BusinessHistoryConsumption;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.repository.user.BusinessHistoryConsumptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessHistoryConsumptionImplementation implements BusinessHistoryConsumptionInterface {

    private final BusinessHistoryConsumptionRepository businessHistoryConsumptionRepository;
    private final BusinessDetailRepository businessDetailRepository;
    private final BusinessDetailHistoryConsumptionMapper businessHistoryConsumptionMapper;

    @Override
    public List<BusinessHistoryConsumptionDTO> findByBusinessDetailId(UUID businessDetailId) {
        return businessHistoryConsumptionRepository.findByBusinessDetail_BusinessDetailId(businessDetailId)
                .stream()
                .map(businessHistoryConsumptionMapper::toDTO)
                .toList();
    }

    @Override
    public BusinessHistoryConsumptionDTO create(BusinessHistoryConsumptionDTO dto) {
        // 🔹 Lấy BusinessDetail từ DB để gắn quan hệ
        BusinessDetail businessDetail = businessDetailRepository.findById(dto.getBusinessDetailId())
                .orElseThrow(() -> new RuntimeException("BusinessDetail not found"));

        // 🔹 Map DTO → Entity
        BusinessHistoryConsumption entity = businessHistoryConsumptionMapper.toEntity(dto);
        entity.setBusinessDetail(businessDetail);

        // 🔹 Lưu xuống DB
        entity = businessHistoryConsumptionRepository.save(entity);

        // 🔹 Trả về DTO
        return businessHistoryConsumptionMapper.toDTO(entity);
    }

    @Override
    public void deleteByBusinessDetailId(UUID businessDetailId) {
        businessHistoryConsumptionRepository.deleteByBusinessDetail_BusinessDetailId(businessDetailId);
    }
}