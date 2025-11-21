package com.EIPplatform.service.report.reportCache;

public interface ReportCacheFactory {
    <T> ReportCacheService<T> getCacheService(Class<T> draftClass);
}