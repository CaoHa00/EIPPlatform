package com.EIPplatform.service.report.wastemanagement;

import java.util.UUID;

import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataUpdateDTO;

public interface WasteManagementDataService {
    /**
     * Tạo WasteManagementData mới từ request và lưu vào draft cache
     */
    WasteManagementDataDTO createWasteManagementData(UUID reportId, WasteManagementDataCreateDTO request);

    /**
     * Cập nhật WasteManagementData từ request và lưu vào draft cache
     */
    WasteManagementDataDTO updateWasteManagementData(UUID reportId, WasteManagementDataUpdateDTO request);

    /**
     * Lấy WasteManagementData từ draft cache
     */
    WasteManagementDataDTO getWasteManagementData(UUID reportId);

    /**
     * Xóa WasteManagementData khỏi draft cache
     */
    void deleteWasteManagementData(UUID reportId);
}