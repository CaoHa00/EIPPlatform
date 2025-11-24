package com.EIPplatform.service.report.reporta05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;

import java.util.UUID;

public interface WasteManagementDataService {
    /**
     * Tạo WasteManagementData mới từ request và lưu vào draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     * @param request Dữ liệu tạo mới
     * @return DTO của WasteManagementData đã tạo
     */
    WasteManagementDataDTO createWasteManagementData(UUID reportId, UUID businessDetailId, WasteManagementDataCreateDTO request);

    /**
     * Lấy WasteManagementData từ draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return DTO của WasteManagementData hoặc null nếu không tồn tại
     */
    WasteManagementDataDTO getWasteManagementData(UUID reportId, UUID userAccountId);

    /**
     * Xóa WasteManagementData khỏi draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     */
    void deleteWasteManagementData(UUID reportId, UUID userAccountId);
}