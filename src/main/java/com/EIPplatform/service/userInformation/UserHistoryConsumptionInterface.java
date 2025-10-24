package com.EIPplatform.service.userInformation;

import java.util.UUID;

import com.EIPplatform.model.dto.userInformation.UserHistoryConsumptionDTO;

public interface UserHistoryConsumptionInterface {
    // Tìm theo ID
    UserHistoryConsumptionDTO findById(Long id);
    
    // Xóa theo ID
    void deleteById(Long id);
    
    // Các method bổ sung thường dùng
    java.util.List<UserHistoryConsumptionDTO> findByUserDetailId(UUID userDetailId);
    
    UserHistoryConsumptionDTO create(UserHistoryConsumptionDTO dto);
    
    UserHistoryConsumptionDTO update(Long id, UserHistoryConsumptionDTO dto);
}
