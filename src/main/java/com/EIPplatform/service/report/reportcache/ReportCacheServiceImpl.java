package com.EIPplatform.service.report.reportcache;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportCacheServiceImpl implements ReportCacheService {
    RedisTemplate<String, String> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final String CACHE_KEY_PREFIX = "report:draft:user:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    /**
     * Build cache key with pattern: report:draft:user:{userAccountId}:report:{reportId}
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
    public void saveDraftReport(ReportA05DraftDTO draft, UUID userAccountId) {
        log.info("Saving draft report to cache - reportId: {}, userAccountId: {}", draft.getReportId(), userAccountId);

        draft.setLastModified(LocalDateTime.now());
        draft.setIsDraft(true);

        String key = buildCacheKey(userAccountId, draft.getReportId());
        try {
            String json = objectMapper.writeValueAsString(draft);
            redisTemplate.opsForValue().set(key, json, CACHE_TTL);
            log.info("Draft report saved to cache with key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize draft to JSON - reportId: {}, userAccountId: {}",
                    draft.getReportId(), userAccountId, e);
            throw new RuntimeException("Serialization error for draft report", e);
        }
    }

    @Override
    public ReportA05DraftDTO getDraftReport(UUID reportId, UUID userAccountId) {
        log.info("Getting draft report from cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        String key = buildCacheKey(userAccountId, reportId);
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                log.warn("Draft report not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
                return null;
            }
            ReportA05DraftDTO draft = objectMapper.readValue(json, ReportA05DraftDTO.class);
            log.info("Draft report found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft;
        } catch (Exception e) {
            log.error("Failed to deserialize draft from cache, deleting corrupted entry - key: {}", key, e);
            redisTemplate.delete(key);
            return null;
        }
    }

    @Override
    public void updateWasteWaterData(UUID reportId, UUID userAccountId, WasteWaterDataDTO wasteWaterDataDTO) {
        log.info("Updating waste water data in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);

        // Lấy draft hiện tại (hoặc tạo mới nếu chưa có)
        ReportA05DraftDTO draft = getDraftReport(reportId, userAccountId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .build();
        }

        // Cập nhật waste water data
        draft.setWasteWaterData(wasteWaterDataDTO);

        // Lưu lại với userAccountId
        saveDraftReport(draft, userAccountId);

        log.info("Updated waste water data in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
    }

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
        log.info("Deleting all draft reports for userAccountId: {}", userAccountId);
        String pattern = buildUserDraftsPattern(userAccountId);
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys != null && !keys.isEmpty()) {
            Long deleted = redisTemplate.delete(keys);
            log.info("Deleted {} draft reports for userAccountId: {}", deleted, userAccountId);
        } else {
            log.info("No draft reports found for userAccountId: {}", userAccountId);
        }
    }
}