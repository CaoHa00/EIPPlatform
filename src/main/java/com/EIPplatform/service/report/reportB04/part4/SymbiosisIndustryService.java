package com.EIPplatform.service.report.reportB04.part4;

import com.EIPplatform.model.dto.report.reportB04.part4.SymbiosisIndustryDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.request.SymbiosisIndustryCreateRequestDTO;

import java.util.UUID;

public interface SymbiosisIndustryService {
    /**
     * Tạo SymbiosisIndustry mới từ request và lưu vào draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     * @param request Dữ liệu tạo mới
     * @return DTO của SymbiosisIndustry đã tạo
     */
    SymbiosisIndustryDTO createReportB04Part4(
            UUID reportId, UUID businessDetailId, SymbiosisIndustryCreateRequestDTO request);

    /**
     * Lấy SymbiosisIndustry từ draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     * @return DTO của SymbiosisIndustry hoặc null nếu không tồn tại
     */
    SymbiosisIndustryDTO getReportB04Part4(UUID reportId, UUID businessDetailId);

    /**
     * Xóa SymbiosisIndustry khỏi draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     */
    void deleteReportB04Part4(UUID reportId, UUID businessDetailId);


}
