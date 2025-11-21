package com.EIPplatform.service.report.reportCache;

import java.util.List;
import java.util.UUID;

/**
 * Generic cache service for report drafts
 * @param <T> The DTO type for the report draft
 */
public interface ReportCacheService<T> {

    /**
     * Save draft report to cache
     */
    void saveDraftReport(T draft, UUID businessDetailId, UUID reportId);

    /**
     * Get draft report from cache
     */
    T getDraftReport(UUID reportId, UUID businessDetailId);

    /**
     * Update specific section data in draft report
     */
    <S> void updateSectionData(UUID reportId, UUID businessDetailId, S sectionData, String sectionName);

    /**
     * Delete draft report from cache
     */
    void deleteDraftReport(UUID reportId, UUID businessDetailId);

    /**
     * Delete all draft reports for a specific user
     */
    void deleteAllDraftsByUser(UUID businessDetailId);

    /**
     * Get all draft reports for a specific user
     */
    List<T> getAllDraftsByUser(UUID businessDetailId);

    /**
     * Check if draft exists in cache
     */
    boolean draftExists(UUID reportId, UUID businessDetailId);
}