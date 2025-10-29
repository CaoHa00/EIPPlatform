package com.EIPplatform.service.report;

import java.util.List;
import java.util.UUID;

import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;

public interface WasteWaterDataService {
  
   /**
     * Cập nhật waste water data vào CACHE (không lưu DB)
     * Dùng khi user đang nhập liệu
 * @throws Exception 
     */
    void updateWasteWaterDataToCache(UUID reportId, WasteWaterDataDTO dto) throws Exception;
    
    /**
     * Lấy waste water data từ CACHE
     */
    WasteWaterDataDTO getWasteWaterDataFromCache(UUID reportId);
    
    /**
     * LƯU TOÀN BỘ REPORT từ cache xuống DATABASE
     * Dùng khi user click "Save/Submit"
     */
    void saveReportFromCacheToDatabase(UUID reportId);
    
    /**
     * Lấy waste water data từ DATABASE (dùng khi load lại report đã submit)
     */
    WasteWaterDataDTO getWasteWaterDataFromDatabase(UUID reportId);
    
    /**
     * Xóa draft khỏi cache
     */
    void deleteDraftFromCache(UUID reportId);
}
