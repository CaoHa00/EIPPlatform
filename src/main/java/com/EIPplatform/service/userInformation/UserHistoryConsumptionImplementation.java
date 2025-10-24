package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.mapper.userInformation.UserHistoryConsumptionMapper;
import com.EIPplatform.model.dto.userInformation.UserHistoryConsumptionDTO;
import com.EIPplatform.model.entity.user.userInformation.UserHistoryConsumption;
import com.EIPplatform.repository.user.UserHistoryConsumptionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHistoryConsumptionImplementation implements UserHistoryConsumptionInterface {

    private final UserHistoryConsumptionRepository userHistoryConsumptionRepository;
    private final UserHistoryConsumptionMapper userHistoryConsumptionMapper;

    @Override
    @Transactional(readOnly = true)
    public UserHistoryConsumptionDTO findById(Long id) {
        UserHistoryConsumption entity = userHistoryConsumptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserHistoryConsumption not found with id: " + id));
        return userHistoryConsumptionMapper.toDTO(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!userHistoryConsumptionRepository.existsById(id)) {
            throw new RuntimeException("UserHistoryConsumption not found with id: " + id);
        }
        userHistoryConsumptionRepository.deleteById(id);
    }

    @Override
    public List<UserHistoryConsumptionDTO> findByUserDetailId(UUID userDetailId) {
        List<UserHistoryConsumption> entities = userHistoryConsumptionRepository.findByUserDetailUserDetailId(userDetailId);
        return userHistoryConsumptionMapper.toDTOList(entities);
    }

    @Override
    public UserHistoryConsumptionDTO create(UserHistoryConsumptionDTO dto) {
        UserHistoryConsumption entity = userHistoryConsumptionMapper.toEntity(dto);
        entity = userHistoryConsumptionRepository.save(entity);
        return userHistoryConsumptionMapper.toDTO(entity);
    }

    @Override
    public UserHistoryConsumptionDTO update(Long id, UserHistoryConsumptionDTO dto) {
        UserHistoryConsumption existing = userHistoryConsumptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserHistoryConsumption not found with id: " + id));

        userHistoryConsumptionMapper.updateEntityFromDto(dto, existing);
        UserHistoryConsumption saved = userHistoryConsumptionRepository.save(existing);
        return userHistoryConsumptionMapper.toDTO(saved);
    }
}
