package com.EIPplatform.service.report.reportCache.reportCacheB04;

import java.util.UUID;

import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;

public interface ReportB04CacheService {
    void saveDraftReport(ReportB04DraftDTO draft, UUID userAccountId);

    ReportB04DraftDTO getDraftReport(UUID reportId, UUID userAccountId);

    // void updateWasteWaterData(UUID reportId, UUID userAccountId, WasteWaterDataDTO wasteWaterDataDTO);

    void deleteDraftReport(UUID reportId, UUID userAccountId);

    // Optional: Xóa tất cả draft của một user
    void deleteAllDraftsByUser(UUID userAccountId);
}
