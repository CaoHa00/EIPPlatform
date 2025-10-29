package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.mapper.businessInformation.BusinessDetailMapper;
import com.EIPplatform.mapper.businessInformation.BusinessDetailWithHistoryConsumptionMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailWithHistoryConsumptionDTO;
import com.EIPplatform.repository.user.BusinessDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BusinessDetailImplementation implements BusinessDetailInterface {
    private final BusinessDetailRepository businessDetailRepository;
    private final BusinessDetailMapper businessDetailMapper;
    private final BusinessDetailWithHistoryConsumptionMapper businessDetailWithHistoryConsumptionMapper;
    @Override
    public BusinessDetailDTO findById(UUID id) {
        BusinessDetailDTO dto = businessDetailRepository.findById(id)
                .map(businessDetailMapper::toDTO)
                .orElse(null);
        return dto;
    }

    @Override
    public void deleteById(UUID id) {
        businessDetailRepository.deleteById(id);
    }

    @Override
    public BusinessDetailDTO create(BusinessDetailDTO dto) {
        var entity = businessDetailMapper.toEntity(dto);
        entity = businessDetailRepository.save(entity);
        return businessDetailMapper.toDTO(entity);
    }

    @Override
    public BusinessDetailDTO update(UUID id, BusinessDetailDTO dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<BusinessDetailDTO> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BusinessDetailWithHistoryConsumptionDTO getBusinessDetailWithHistoryConsumption(UUID id) {
        var result = businessDetailWithHistoryConsumptionMapper.toDTO(businessDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found")));
        return result;
    }
}
