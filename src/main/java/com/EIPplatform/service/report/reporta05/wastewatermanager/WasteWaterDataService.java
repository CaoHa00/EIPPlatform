package com.EIPplatform.service.report.reporta05.wastewatermanager;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface WasteWaterDataService {
    /**
     * Tạo WasteWaterData mới từ request và lưu vào draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @param request Dữ liệu tạo mới
     * @param connectionFile File sơ đồ đấu nối (optional)
     * @param mapFile File bản đồ trạm tự động (optional)
     * @return DTO của WasteWaterData đã tạo
     */
    WasteWaterDataDTO createWasteWaterData(UUID reportId, UUID userAccountId, WasteWaterDataCreateDTO request,
                                           MultipartFile connectionFile, MultipartFile mapFile);

    /**
     * Lấy WasteWaterData từ draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return DTO của WasteWaterData hoặc null nếu không tồn tại
     */
    WasteWaterDataDTO getWasteWaterData(UUID reportId, UUID userAccountId);

    /**
     * Xóa WasteWaterData khỏi draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     */
    void deleteWasteWaterData(UUID reportId, UUID userAccountId);

    /**
     * Xóa file sơ đồ đấu nối khỏi draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     */
    void deleteWasteWaterDataConnectionFile(UUID reportId, UUID userAccountId);

    /**
     * Xóa file bản đồ trạm tự động khỏi draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     */
    void deleteWasteWaterDataMapFile(UUID reportId, UUID userAccountId);

    /**
     * Tải xuống file sơ đồ đấu nối từ draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return Resource của file
     */
    Resource downloadWasteWaterDataConnectionFile(UUID reportId, UUID userAccountId);

    /**
     * Tải xuống file bản đồ trạm tự động từ draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return Resource của file
     */
    Resource downloadWasteWaterDataMapFile(UUID reportId, UUID userAccountId);

    /**
     * Kiểm tra sự tồn tại của file sơ đồ đấu nối trong draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return true nếu file tồn tại, false nếu không
     */
    boolean hasWasteWaterDataConnectionFile(UUID reportId, UUID userAccountId);

    /**
     * Kiểm tra sự tồn tại của file bản đồ trạm tự động trong draft cache
     * @param reportId ID của báo cáo
     * @param userAccountId ID của người dùng
     * @return true nếu file tồn tại, false nếu không
     */
    boolean hasWasteWaterDataMapFile(UUID reportId, UUID userAccountId);
}