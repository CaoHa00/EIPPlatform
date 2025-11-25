package com.EIPplatform.service.report.reportCache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Generic implementation of ReportCacheService
 * 
 * @param <T> The DTO type for the report draft
 */
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportCacheServiceImpl<T> implements ReportCacheService<T> {

    RedisTemplate<String, String> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    Class<T> draftClass;

    public ReportCacheServiceImpl(RedisTemplate<String, String> redisTemplate, Class<T> draftClass) {
        this.redisTemplate = redisTemplate;
        this.draftClass = draftClass;
    }

    private static final String CACHE_KEY_PREFIX = "report:draft:business:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    /**
     * Build cache key with pattern:
     * report:draft:user:{userAccountId}:report:{reportId}
     */
    private String buildCacheKey(UUID businessDetailId, UUID reportId) {
        return CACHE_KEY_PREFIX + businessDetailId.toString() + ":report:" + reportId.toString();
    }

    /**
     * Build pattern to find all drafts of a user
     */
    private String buildUserDraftsPattern(UUID businessDetailId) {
        return CACHE_KEY_PREFIX + businessDetailId.toString() + ":report:*";
    }

    @Override
    public void saveDraftReport(T draft, UUID businessDetailId, UUID reportId) {
        log.info("Saving draft report to cache - reportId: {}, businessDetailId: {}, type: {}",
                reportId, businessDetailId, draftClass.getSimpleName());

        String key = buildCacheKey(businessDetailId, reportId);
        try {
            String json = objectMapper.writeValueAsString(draft);
            redisTemplate.opsForValue().set(key, json, CACHE_TTL);
            log.info("Draft report saved to cache with key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize draft to JSON - reportId: {}, userAccountId: {}",
                    reportId, businessDetailId, e);
            throw new RuntimeException("Serialization error for draft report", e);
        }
    }

    @Override
    public T getDraftReport(UUID reportId, UUID businessDetailId) {
        log.info("Getting draft report from cache - reportId: {}, businessDetailId: {}, type: {}",
                reportId, businessDetailId, draftClass.getSimpleName());
        String key = buildCacheKey(businessDetailId, reportId);
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                log.warn("Draft report not found in cache - reportId: {}, businessDetailId: {}",
                        reportId, businessDetailId);
                return null;
            }
            T draft = objectMapper.readValue(json, draftClass);
            log.info("Draft report found in cache - reportId: {}, businessDetailId: {}",
                    reportId, businessDetailId);
            return draft;
        } catch (Exception e) {
            log.error("Failed to deserialize draft from cache, deleting corrupted entry - key: {}",
                    key, e);
            redisTemplate.delete(key);
            return null;
        }
    }

    @Override
    public <S> void updateSectionData(UUID reportId, UUID businessDetailId, S sectionData, String sectionName) {
        log.info("Updating section '{}' in cache - reportId: {}, businessDetailId: {}",
                sectionName, reportId, businessDetailId);

        T draft = getDraftReport(reportId, businessDetailId);
        if (draft == null) {
            log.warn("Draft not found for update - reportId: {}, businessDetailId: {}",
                    reportId, businessDetailId);
            throw new RuntimeException("Draft report not found for update");
        }

        try {
            String setterMethod = "set" + capitalize(sectionName);
            draft.getClass()
                    .getMethod(setterMethod, sectionData.getClass())
                    .invoke(draft, sectionData);

            saveDraftReport(draft, businessDetailId, reportId);
            log.info("Updated section '{}' in cache - reportId: {}, businessDetailId: {}",
                    sectionName, reportId, businessDetailId);
        } catch (Exception e) {
            log.error("Failed to update section data - reportId: {}, businessDetailId: {}, section: {}",
                    reportId, businessDetailId, sectionName, e);
            throw new RuntimeException("Failed to update section data", e);
        }
    }

    @Override
    public void deleteDraftReport(UUID reportId, UUID businessDetailId) {
        log.info("Deleting draft report from cache - reportId: {}, businessDetailId: {}",
                reportId, businessDetailId);
        String key = buildCacheKey(businessDetailId, reportId);
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.info("Draft report deleted from cache - reportId: {}, businessDetailId: {}",
                    reportId, businessDetailId);
        } else {
            log.warn("Draft report not found for deletion - reportId: {}, businessDetailId: {}",
                    reportId, businessDetailId);
        }
    }

    @Override
    public void deleteAllDraftsByUser(UUID businessDetailId) {
        log.info("Deleting all draft reports for businessDetailId: {}", businessDetailId);

        String pattern = buildUserDraftsPattern(businessDetailId);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(1000).build();

        RedisConnectionFactory factory = Objects.requireNonNull(
                redisTemplate.getConnectionFactory(),
                "RedisConnectionFactory is null");

        try (RedisConnection connection = factory.getConnection();
                Cursor<byte[]> cursor = connection.keyCommands().scan(scanOptions)) {

            RedisKeyCommands keyCommands = connection.keyCommands();
            long deletedCount = 0;

            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                keyCommands.del(key);
                deletedCount++;
            }

            log.info("Deleted {} draft reports for businessDetailId: {}", deletedCount, businessDetailId);
        }
    }

    @Override
    public List<T> getAllDraftsByUser(UUID businessDetailId) {
        log.info("Getting all draft reports for businessDetailId: {}", businessDetailId);

        List<T> drafts = new ArrayList<>();
        String pattern = buildUserDraftsPattern(businessDetailId);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(1000).build();

        RedisConnectionFactory factory = Objects.requireNonNull(
                redisTemplate.getConnectionFactory(),
                "RedisConnectionFactory is null");

        try (RedisConnection connection = factory.getConnection();
                Cursor<byte[]> cursor = connection.keyCommands().scan(scanOptions)) {

            while (cursor.hasNext()) {
                byte[] keyBytes = cursor.next();
                String key = new String(keyBytes);

                try {
                    String json = redisTemplate.opsForValue().get(key);
                    if (json != null) {
                        T draft = objectMapper.readValue(json, draftClass);
                        drafts.add(draft);
                    }
                } catch (Exception e) {
                    log.error("Failed to deserialize draft from key: {}", key, e);
                }
            }

            log.info("Found {} draft reports for businessDetailId: {}", drafts.size(), businessDetailId);
        }

        return drafts;
    }

    @Override
    public boolean draftExists(UUID reportId, UUID businessDetailId) {
        String key = buildCacheKey(businessDetailId, reportId);
        Boolean exists = redisTemplate.hasKey(key);
        return Boolean.TRUE.equals(exists);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}