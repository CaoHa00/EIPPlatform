package com.EIPplatform.service.report.reportB04.part3;

import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionCreateRequestDTO;

import java.util.UUID;

public interface ResourcesSavingAndReductionService {
    /**
     * Tạo ResourcesSavingAndReduction mới từ request và lưu vào draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     * @param request Dữ liệu tạo mới
     * @return DTO của ResourcesSavingAndReduction đã tạo
     */
    ResourcesSavingAndReductionDTO createReportB04Part3(
            UUID reportId, UUID businessDetailId, ResourcesSavingAndReductionCreateRequestDTO request);

    /**
     * Lấy ResourcesSavingAndReduction từ draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     * @return DTO của ResourcesSavingAndReduction hoặc null nếu không tồn tại
     */
    ResourcesSavingAndReductionDTO getReportB04Part3(UUID reportId, UUID businessDetailId);

    /**
     * Xóa ResourcesSavingAndReduction khỏi draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     */
    void deleteReportB04Part3(UUID reportId, UUID businessDetailId);


}
