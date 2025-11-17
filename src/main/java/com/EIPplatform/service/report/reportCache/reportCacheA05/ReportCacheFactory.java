package com.EIPplatform.service.report.reportCache.reportCacheA05;

public interface ReportCacheFactory {
    <T> ReportCacheService<T> getCacheService(Class<T> draftClass);
}