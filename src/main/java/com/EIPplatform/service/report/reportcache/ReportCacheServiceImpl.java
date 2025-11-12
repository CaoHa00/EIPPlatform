package com.EIPplatform.service.report.reportcache;

import java.time.Duration;
import java.time.LocalDateTime;
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
    RedisTemplate<String, String> redisTemplate; // Changed to String,String for JSON storage
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Shared instance
    private static final String CACHE_KEY_PREFIX = "report:draft:"; // Fixed leading space
    private static final Duration CACHE_TTL = Duration.ofHours(24); // lưu trong 24 tiếng

    private String buildCacheKey(UUID reportId) {
        return CACHE_KEY_PREFIX + reportId.toString();
    }

    @Override
    public void saveDraftReport(ReportA05DraftDTO draft) {
        log.info("Saving draft report to cache: {}", draft.getReportId());

        draft.setLastModified(LocalDateTime.now());
        draft.setIsDraft(true);

        String key = buildCacheKey(draft.getReportId());
        try {
            String json = objectMapper.writeValueAsString(draft);
            redisTemplate.opsForValue().set(key, json, CACHE_TTL);
            log.info("Draft report saved to cache with key: {}", key);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize draft to JSON: {}", draft.getReportId(), e);
            throw new RuntimeException("Serialization error for draft report", e);
        }
    }

    @Override
    public ReportA05DraftDTO getDraftReport(UUID reportId) {
        log.info("Getting draft report from cache: {}", reportId);
        String key = buildCacheKey(reportId);
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                log.warn("Draft report not found in cache: {}", reportId);
                return null;
            }
            ReportA05DraftDTO draft = objectMapper.readValue(json, ReportA05DraftDTO.class);
            return draft;
        } catch (Exception e) {
            // Handle deserialization error - delete corrupted cache
            log.error("Failed to deserialize draft from cache, deleting corrupted entry: {}", key, e);
            redisTemplate.delete(key);
            return null;
        }
    }

    @Override
    public void updateWasteWaterData(UUID reportId, WasteWaterDataDTO wasteWaterDataDTO) {
        log.info("Updating waste water data in cache: {}", reportId);

        // Lấy draft hiện tại (hoặc tạo mới nếu chưa có)
        ReportA05DraftDTO draft = getDraftReport(reportId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .build();
        }

        // Cập nhật chỉ waste water data (fixed: only set wasteWaterData)
        draft.setWasteWaterData(wasteWaterDataDTO);
        // Lưu lại
        saveDraftReport(draft);

        log.info("Updated waste water data in cache for report: {}", reportId);
    }

    @Override
    public void deleteDraftReport(UUID reportId) {
        log.info("Deleting draft report from cache: {}", reportId);
        String key = buildCacheKey(reportId);
        redisTemplate.delete(key);
        log.info("Draft report deleted from cache: {}", reportId);
    }
}