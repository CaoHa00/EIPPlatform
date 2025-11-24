package com.EIPplatform.service.report.reporta05.airemmissionmanagement;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface AirEmissionDataService {
    /**
     * Tạo AirEmissionData mới từ request và lưu vào draft cache
     * @param reportId ID của báo cáo
     * @param businessDetailId ID của người dùng
     * @param request Dữ liệu tạo mới
     * @param file File bản đồ trạm (optional)
     * @return DTO của AirEmissionData đã tạo
     */
    AirEmissionDataDTO createAirEmissionData(UUID reportId, UUID businessDetailId, AirEmissionDataCreateDTO request, MultipartFile file);

    /**
     * Lấy AirEmissionData từ draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return DTO của AirEmissionData hoặc null nếu không tồn tại
     */
    AirEmissionDataDTO getAirEmissionData(UUID reportId, UUID userAccountId);

    /**
     * Xóa AirEmissionData khỏi draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     */
    void deleteAirEmissionData(UUID reportId, UUID userAccountId);

    /**
     * Delete map file for AirEmissionData (separate endpoint)
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     */
    void deleteAirEmissionDataFile(UUID reportId, UUID userAccountId);

    /**
     * Download map file for AirEmissionData
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return Resource của file
     */
    Resource downloadAirEmissionDataFile(UUID reportId, UUID userAccountId);

    /**
     * Check if map file exists for AirEmissionData
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return true nếu file tồn tại, false nếu không
     */
    boolean hasAirEmissionDataFile(UUID reportId, UUID userAccountId);
}