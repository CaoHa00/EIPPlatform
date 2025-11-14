package com.EIPplatform.service.report.reportcache.reportCacheB04;

import java.util.UUID;

import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;

public interface ReportB04CacheService {
    void saveDraftReport(ReportA05DraftDTO draft, UUID userAccountId);

    ReportA05DraftDTO getDraftReport(UUID reportId, UUID userAccountId);

    void updateWasteWaterData(UUID reportId, UUID userAccountId, WasteWaterDataDTO wasteWaterDataDTO);

    void deleteDraftReport(UUID reportId, UUID userAccountId);

    // Optional: Xóa tất cả draft của một user
    void deleteAllDraftsByUser(UUID userAccountId);
}