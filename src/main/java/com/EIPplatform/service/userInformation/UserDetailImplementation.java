package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.mapper.userInformation.UserDetailMapper;
import com.EIPplatform.mapper.userInformation.UserDetailWithHistoryConsumptionMapper;
import com.EIPplatform.model.dto.userInformation.UserDetailDTO;
import com.EIPplatform.model.dto.userInformation.UserDetailWithHistoryConsumptionDTO;
import com.EIPplatform.repository.user.BussinessDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailImplementation implements UserDetailInterface {
    private final BussinessDetailRepository bussinessDetailRepository;
    private final UserDetailMapper userDetailMapper;
    private final UserDetailWithHistoryConsumptionMapper userDetailWithHistoryConsumptionMapper;
    @Override
    public UserDetailDTO findById(UUID id) {
        UserDetailDTO dto = bussinessDetailRepository.findById(id)
                .map(userDetailMapper::toDTO)
                .orElse(null);
        return dto;
    }

    @Override
    public void deleteById(UUID id) {
        bussinessDetailRepository.deleteById(id);
    }

    @Override
    public UserDetailDTO create(UserDetailDTO dto) {
        var entity = userDetailMapper.toEntity(dto);
        entity = bussinessDetailRepository.save(entity);
        return userDetailMapper.toDTO(entity);
    }

    @Override
    public UserDetailDTO update(UUID id, UserDetailDTO dto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<UserDetailDTO> findAll() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserDetailWithHistoryConsumptionDTO getUserDetailWithHistoryConsumption(UUID id) {
        var result = userDetailWithHistoryConsumptionMapper.toDTO(bussinessDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
        return result;
    }
}
