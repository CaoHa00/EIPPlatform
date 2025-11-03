package com.EIPplatform.service.report.reportcache;

import java.util.UUID;

import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;

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
    void updateWasteWaterData(UUID reportId, ReportA05DraftDTO wasteWaterData);
    
    //  * Xóa draft khỏi cache (sau khi save vào DB)
    //  */
  
}
