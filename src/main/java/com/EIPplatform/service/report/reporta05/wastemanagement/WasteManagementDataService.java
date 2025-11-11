package com.EIPplatform.service.report.reporta05.wastemanagement;

import java.util.UUID;

import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;

public interface WasteManagementDataService {
    /**
     * Tạo WasteManagementData mới từ request và lưu vào draft cache
     */
    WasteManagementDataDTO createWasteManagementData(UUID reportId, WasteManagementDataCreateDTO request);

    /**
     * Lấy WasteManagementData từ draft cache
     */
    WasteManagementDataDTO getWasteManagementData(UUID reportId);

    /**
     * Xóa WasteManagementData khỏi draft cache
     */
    void deleteWasteManagementData(UUID reportId);
}