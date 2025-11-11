package com.EIPplatform.service.report.reporta05.wastewatermanager;


import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface WasteWaterDataService {
    /**
     * Tạo WasteWaterData mới từ request và lưu vào draft cache
     */
    WasteWaterDataDTO createWasteWaterData(UUID reportId, WasteWaterDataCreateDTO request, MultipartFile connectionFile, MultipartFile mapFile);

    /**
     * Lấy WasteWaterData từ draft cache
     */
    WasteWaterDataDTO getWasteWaterData(UUID reportId);

    /**
     * Xóa WasteWaterData khỏi draft cache
     */
    void deleteWasteWaterData(UUID reportId);

    /**
     * Xóa file sơ đồ đấu nối khỏi draft cache
     */
    void deleteWasteWaterDataConnectionFile(UUID reportId);

    /**
     * Xóa file bản đồ trạm tự động khỏi draft cache
     */
    void deleteWasteWaterDataMapFile(UUID reportId);

    /**
     * Tải xuống file sơ đồ đấu nối từ draft cache
     */
    Resource downloadWasteWaterDataConnectionFile(UUID reportId);

    /**
     * Tải xuống file bản đồ trạm tự động từ draft cache
     */
    Resource downloadWasteWaterDataMapFile(UUID reportId);

    /**
     * Kiểm tra sự tồn tại của file sơ đồ đấu nối trong draft cache
     */
    boolean hasWasteWaterDataConnectionFile(UUID reportId);

    /**
     * Kiểm tra sự tồn tại của file bản đồ trạm tự động trong draft cache
     */
    boolean hasWasteWaterDataMapFile(UUID reportId);
}