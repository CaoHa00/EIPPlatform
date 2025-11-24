package com.EIPplatform.service.report.reportB04.part3;

import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionCreateRequestDTO;

import java.util.UUID;

public interface ResourcesSavingAndReductionService {
    /**
     * Tạo ResourcesSavingAndReduction mới từ request và lưu vào draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @param request Dữ liệu tạo mới
     * @return DTO của ResourcesSavingAndReduction đã tạo
     */
    ResourcesSavingAndReductionDTO createResourcesSavingAndReduction(
            UUID reportId, UUID userAccountId, ResourcesSavingAndReductionCreateRequestDTO request);

    /**
     * Lấy ResourcesSavingAndReduction từ draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return DTO của ResourcesSavingAndReduction hoặc null nếu không tồn tại
     */
    ResourcesSavingAndReductionDTO getResourcesSavingAndReduction(UUID reportId, UUID userAccountId);

    /**
     * Xóa ResourcesSavingAndReduction khỏi draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     */
    void deleteResourcesSavingAndReduction(UUID reportId, UUID userAccountId);


}
