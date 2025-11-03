package com.EIPplatform.service.report.reportcache;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;
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
    RedisTemplate<String, Object> redisTemplate;
    private static final String CACHE_KEY_PREFIX = " report:draft:";
    private static final Duration  CACHE_TTL = Duration.ofHours(24); // luu trong 24 tieng

    private String buildCacheKey(UUID reportId){
        return CACHE_KEY_PREFIX + reportId.toString();
    }

    @Override
    public void saveDraftReport(ReportA05DraftDTO draft) {
        log.info("Saving draft report to cache: {}",draft.getReportId());

        draft.setLastModified(LocalDateTime.now());
        draft.setIsDraft(true);

        String key = buildCacheKey(draft.getReportId());
        redisTemplate.opsForValue().set(key, draft, CACHE_TTL);
        
        log.info("Draft report saved to chache with key:{}", key);

    }

    @Override
    public ReportA05DraftDTO getDraftReport(UUID reportId) {
        log.info("Getting draft report from cache: {}", reportId);
        String key = buildCacheKey(reportId);
        try {
            Object cached = redisTemplate.opsForValue().get(key);
            
            if (cached == null) {
                log.warn("Draft report not found in cache: {}", reportId);
                return null;
            }
            
            // ✅ Handle LinkedHashMap (old data format)
            if (cached instanceof LinkedHashMap) {
                log.warn("Found LinkedHashMap in cache, converting to DTO");
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                return mapper.convertValue(cached, ReportA05DraftDTO.class);
            }
            
            return (ReportA05DraftDTO) cached;
            
        } catch (Exception e) {
            // ✅ Handle deserialization error - delete corrupted cache
            log.error("Failed to deserialize draft from cache, deleting corrupted entry: {}", key, e);
            redisTemplate.delete(key);
            return null;
        }
    }

    
    @Override
   public void updateWasteWaterData(UUID reportId, ReportA05DraftDTO wasteWaterData) {
        log.info("Updating waste water data in cache: {}", reportId);
        
        // Lấy draft hiện tại (hoặc tạo mới nếu chưa có)
        ReportA05DraftDTO draft = getDraftReport(reportId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                .reportId(reportId)
                .build();
        }
        
        // Cập nhật waste water data
        draft.setWasteWaterData(wasteWaterData.getWasteWaterData());
        draft.setWasteManagementData(wasteWaterData.getWasteManagementData());
        // Lưu lại
        saveDraftReport(draft);
        
        log.info("Updated waste water data in cache for report: {}", reportId);
    }

}
