package com.EIPplatform.service.report;

import java.util.UUID;

import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05DraftDTO;

public interface ReportCacheService {
    /**
     * Lưu draft report vào cache
     */
    void saveDraftReport(ReportA05DraftDTO draft);
    
    /**
     * Lấy draft report từ cache
     */
    ReportA05DraftDTO getDraftReport(UUID reportId);
    
    /**
     * Cập nhật phần waste water vào draft
     */
    void updateWasteWaterData(UUID reportId, WasteWaterDataDTO wasteWaterData);
    
    /**
     * Xóa draft khỏi cache (sau khi save vào DB)
     */
    void deleteDraftReport(UUID reportId);
    
    /**
     * Kiểm tra draft có tồn tại không
     */
    boolean existsDraftReport(UUID reportId);
    
    /**
     * Tính % hoàn thành
     */
    Integer calculateCompletionPercentage(UUID reportId);
}
