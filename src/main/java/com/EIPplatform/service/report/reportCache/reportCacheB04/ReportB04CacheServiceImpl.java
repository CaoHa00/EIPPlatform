package com.EIPplatform.service.report.reportCache.reportCacheB04;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisKeyCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportB04CacheServiceImpl implements ReportB04CacheService {

    RedisTemplate<String, String> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final String CACHE_KEY_PREFIX = "reportB04:draft:user:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    /**
     * Build cache key with pattern:
     * reportB04:draft:user:{userAccountId}:reportB04:{reportId}
     */
    private String buildCacheKey(UUID userAccountId, UUID reportId) {
        return CACHE_KEY_PREFIX + userAccountId.toString() + ":reportB04:" + reportId.toString();
    }

    /**
     * Build pattern to find all drafts of a user
     */
    private String buildUserDraftsPattern(UUID userAccountId) {
        return CACHE_KEY_PREFIX + userAccountId.toString() + ":reportB04:*";
    }

    @Override
    public void saveDraftReport(ReportB04DraftDTO draft, UUID userAccountId) {
        log.info("Saving draft reportB04 to cache - reportId: {}, userAccountId: {}", draft.getReportId(), userAccountId);

        draft.setLastModified(LocalDateTime.now());
        draft.setIsDraft(true);

        String key = buildCacheKey(userAccountId, draft.getReportId());
        try {
            String json = objectMapper.writeValueAsString(draft);
            redisTemplate.opsForValue().set(key, json, CACHE_TTL);
            log.info("Draft reportB04 saved to cache with key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize draft to JSON - reportId: {}, userAccountId: {}",
                    draft.getReportId(), userAccountId, e);
            throw new RuntimeException("Serialization error for draft reportB04", e);
        }
    }

    @Override
    public ReportB04DraftDTO getDraftReport(UUID reportId, UUID userAccountId) {
        log.info("Getting draft reportB04 from cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        String key = buildCacheKey(userAccountId, reportId);
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                log.warn("Draft reportB04 not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
                return null;
            }
            ReportB04DraftDTO draft = objectMapper.readValue(json, ReportB04DraftDTO.class);
            log.info("Draft reportB04 found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft;
        } catch (Exception e) {
            log.error("Failed to deserialize draft from cache, deleting corrupted entry - key: {}", key, e);
            redisTemplate.delete(key);
            return null;
        }
    }

    // @Override
    // public void updateWasteWaterData(UUID reportId, UUID userAccountId, WasteWaterDataDTO wasteWaterDataDTO) {
    //     log.info("Updating waste water data in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);

    //     // Lấy draft hiện tại (hoặc tạo mới nếu chưa có)
    //     ReportA05DraftDTO draft = getDraftReport(reportId, userAccountId);
    //     if (draft == null) {
    //         draft = ReportA05DraftDTO.builder()
    //                 .reportId(reportId)
    //                 .build();
    //     }

    //     // Cập nhật waste water data
    //     draft.setWasteWaterData(wasteWaterDataDTO);

    //     // Lưu lại với userAccountId
    //     saveDraftReport(draft, userAccountId);

    //     log.info("Updated waste water data in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
    // }

    @Override
    public void deleteDraftReport(UUID reportId, UUID userAccountId) {
        log.info("Deleting draft report from cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        String key = buildCacheKey(userAccountId, reportId);
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.info("Draft report deleted from cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        } else {
            log.warn("Draft report not found for deletion - reportId: {}, userAccountId: {}", reportId, userAccountId);
        }
    }

    @Override
    public void deleteAllDraftsByUser(UUID userAccountId) {
        log.info("Deleting all draft reportB04s for userAccountId: {}", userAccountId);

        String pattern = buildUserDraftsPattern(userAccountId);
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).count(1000).build();

        RedisConnectionFactory factory
                = Objects.requireNonNull(redisTemplate.getConnectionFactory(), "RedisConnectionFactory is null");

        try (RedisConnection connection = factory.getConnection(); Cursor<byte[]> cursor = connection.keyCommands().scan(scanOptions)) {

            RedisKeyCommands keyCommands = connection.keyCommands();
            long deletedCount = 0;

            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                keyCommands.del(key);
                deletedCount++;
            }

            log.info("Deleted {} draft reportB04 for userAccountId: {}", deletedCount, userAccountId);
        }
    }
}
