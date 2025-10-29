package com.EIPplatform.service.report;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05DraftDTO;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ReportCacheServiceImpl implements ReportCacheService{
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
        Object cached = redisTemplate.opsForValue().get(key);
        
        if(cached == null) {
            log.warn("Dart report not found in cache: {}", reportId);
            return null;
        }
        return (ReportA05DraftDTO) cached;
    }

    @Override
    public void updateWasteWaterData(UUID reportId, WasteWaterDataDTO wasteWaterData) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateWasteWaterData'");
    }

    @Override
    public void deleteDraftReport(UUID reportId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteDraftReport'");
    }

    @Override
    public boolean existsDraftReport(UUID reportId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsDraftReport'");
    }

    @Override
    public Integer calculateCompletionPercentage(UUID reportId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calculateCompletionPercentage'");
    }
    
}
