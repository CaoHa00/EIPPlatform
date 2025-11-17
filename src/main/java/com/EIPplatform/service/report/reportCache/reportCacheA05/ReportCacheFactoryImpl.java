package com.EIPplatform.service.report.reportCache.reportCacheA05;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class  ReportCacheFactoryImpl implements ReportCacheFactory {

    private final RedisTemplate<String, String> redisTemplate;
    private final Map<Class<?>, ReportCacheService<?>> cacheServices = new ConcurrentHashMap<>(); // Cache instances để reuse

    public ReportCacheFactoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ReportCacheService<T> getCacheService(Class<T> draftClass) {
        return (ReportCacheService<T>) cacheServices.computeIfAbsent(draftClass, clazz ->
                new ReportCacheServiceImpl<>(redisTemplate, draftClass)
        );
    }
}