package com.EIPplatform.service.report.reportCache.reportCacheA05;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

/**
 * Generic implementation of ReportCacheService
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

    private static final String CACHE_KEY_PREFIX = "report:draft:user:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    /**
     * Build cache key with pattern:
     * report:draft:user:{userAccountId}:report:{reportId}
     */
    private String buildCacheKey(UUID userAccountId, UUID reportId) {
        return CACHE_KEY_PREFIX + userAccountId.toString() + ":report:" + reportId.toString();
    }

    /**
     * Build pattern to find all drafts of a user
     */
    private String buildUserDraftsPattern(UUID userAccountId) {
        return CACHE_KEY_PREFIX + userAccountId.toString() + ":report:*";
    }

    @Override
    public void saveDraftReport(T draft, UUID userAccountId, UUID reportId) {
        log.info("Saving draft report to cache - reportId: {}, userAccountId: {}, type: {}",
                reportId, userAccountId, draftClass.getSimpleName());

        String key = buildCacheKey(userAccountId, reportId);
        try {
            String json = objectMapper.writeValueAsString(draft);
            redisTemplate.opsForValue().set(key, json, CACHE_TTL);
            log.info("Draft report saved to cache with key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize draft to JSON - reportId: {}, userAccountId: {}",
                    reportId, userAccountId, e);
            throw new RuntimeException("Serialization error for draft report", e);
        }
    }

    @Override
    public T getDraftReport(UUID reportId, UUID userAccountId) {
        log.info("Getting draft report from cache - reportId: {}, userAccountId: {}, type: {}",
                reportId, userAccountId, draftClass.getSimpleName());
        String key = buildCacheKey(userAccountId, reportId);
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                log.warn("Draft report not found in cache - reportId: {}, userAccountId: {}",
                        reportId, userAccountId);
                return null;
            }
            T draft = objectMapper.readValue(json, draftClass);
            log.info("Draft report found in cache - reportId: {}, userAccountId: {}",
                    reportId, userAccountId);
            return draft;
        } catch (Exception e) {
            log.error("Failed to deserialize draft from cache, deleting corrupted entry - key: {}",
                    key, e);
            redisTemplate.delete(key);
            return null;
        }
    }

    @Override
    public <S> void updateSectionData(UUID reportId, UUID userAccountId, S sectionData, String sectionName) {
        log.info("Updating section '{}' in cache - reportId: {}, userAccountId: {}",
                sectionName, reportId, userAccountId);

        T draft = getDraftReport(reportId, userAccountId);
        if (draft == null) {
            log.warn("Draft not found for update - reportId: {}, userAccountId: {}",
                    reportId, userAccountId);
            throw new RuntimeException("Draft report not found for update");
        }

        try {
            String setterMethod = "set" + capitalize(sectionName);
            draft.getClass()
                    .getMethod(setterMethod, sectionData.getClass())
                    .invoke(draft, sectionData);

            saveDraftReport(draft, userAccountId, reportId);
            log.info("Updated section '{}' in cache - reportId: {}, userAccountId: {}",
                    sectionName, reportId, userAccountId);
        } catch (Exception e) {
            log.error("Failed to update section data - reportId: {}, userAccountId: {}, section: {}",
                    reportId, userAccountId, sectionName, e);
            throw new RuntimeException("Failed to update section data", e);
        }
    }

    @Override
    public void deleteDraftReport(UUID reportId, UUID userAccountId) {
        log.info("Deleting draft report from cache - reportId: {}, userAccountId: {}",
                reportId, userAccountId);
        String key = buildCacheKey(userAccountId, reportId);
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.info("Draft report deleted from cache - reportId: {}, userAccountId: {}",
                    reportId, userAccountId);
        } else {
            log.warn("Draft report not found for deletion - reportId: {}, userAccountId: {}",
                    reportId, userAccountId);
        }
    }

    @Override
    public void deleteAllDraftsByUser(UUID userAccountId) {
        log.info("Deleting all draft reports for userAccountId: {}", userAccountId);

        String pattern = buildUserDraftsPattern(userAccountId);
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

            log.info("Deleted {} draft reports for userAccountId: {}", deletedCount, userAccountId);
        }
    }

    @Override
    public List<T> getAllDraftsByUser(UUID userAccountId) {
        log.info("Getting all draft reports for userAccountId: {}", userAccountId);

        List<T> drafts = new ArrayList<>();
        String pattern = buildUserDraftsPattern(userAccountId);
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

            log.info("Found {} draft reports for userAccountId: {}", drafts.size(), userAccountId);
        }

        return drafts;
    }

    @Override
    public boolean draftExists(UUID reportId, UUID userAccountId) {
        String key = buildCacheKey(userAccountId, reportId);
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