package com.EIPplatform.service.userInformation;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.userInformation.UserDetailDTO;
import com.EIPplatform.model.dto.userInformation.UserDetailWithHistoryConsumptionDTO;

public interface  UserDetailInterface {
    UserDetailDTO findById(UUID id);
    void deleteById(UUID id);
    UserDetailDTO create(UserDetailDTO dto);
    UserDetailDTO update(UUID id, UserDetailDTO dto);
    List<UserDetailDTO> findAll();
    UserDetailWithHistoryConsumptionDTO getUserDetailWithHistoryConsumption(UUID id);
}
