package com.EIPplatform.service.authentication;

import java.time.Duration;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.EIPplatform.properties.RateLimitProperties;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisRateLimitService {

    @Qualifier("rateLimitRedisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    RateLimitProperties rateLimitProperties;

    static final String RATE_LIMIT_PREFIX = "rate_limit:";

    public RateLimitResult checkRateLimit(String userId, String ruleName) {
        if (!rateLimitProperties.isEnabled()) {
            return RateLimitResult.allowed();
        }

        RateLimitProperties.RateLimitRule rule = rateLimitProperties.getRules().get(ruleName);
        if (rule == null) {
            rule = rateLimitProperties.getRules().get(rateLimitProperties.getDefaultRule());
        }

        String key = RATE_LIMIT_PREFIX + ruleName + ":" + userId;
        return executeRateLimitCheck(key, rule);
    }

    private RateLimitResult executeRateLimitCheck(String key, RateLimitProperties.RateLimitRule rule) {
        try {
            long currentTime = System.currentTimeMillis();
            long windowStartTime = currentTime - (rule.getWindowSeconds() * 1000);

            redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStartTime);

            Long currentCount = redisTemplate.opsForZSet().count(key, windowStartTime, currentTime);
            long count = currentCount != null ? currentCount : 0;
            if (count >= rule.getMaxRequests()) {
                Set<Object> oldestEntries = redisTemplate.opsForZSet().range(key, 0, 0);
                long resetTime = currentTime + (rule.getWindowSeconds() * 1000);

                if (!oldestEntries.isEmpty()) {
                    try {
                        long oldestRequestTime = Long.parseLong(oldestEntries.iterator().next().toString());
                        resetTime = oldestRequestTime + (rule.getWindowSeconds() * 1000);
                    } catch (NumberFormatException e) {
                        log.warn("Error parsing oldest request time for key: {}", key);
                    }
                }

                return RateLimitResult.blocked(
                        rule.getMaxRequests(),
                        0,
                        resetTime,
                        rule.getDescription());
            }

            redisTemplate.opsForZSet().add(key, String.valueOf(currentTime), currentTime);

            redisTemplate.expire(key, Duration.ofSeconds(rule.getWindowSeconds() + 10));

            long remaining = rule.getMaxRequests() - count - 1;
            long resetTime = currentTime + (rule.getWindowSeconds() * 1000);

            return RateLimitResult.allowed(
                    rule.getMaxRequests(),
                    Math.max(0, remaining),
                    resetTime,
                    rule.getDescription());

        } catch (Exception e) {
            log.error("Error checking rate limit for key: {}", key, e);
            return RateLimitResult.allowed();
        }
    }

    public RateLimitResult checkRateLimitByIp(String ipAddress, String ruleName) {
        if (!rateLimitProperties.isEnabled()) {
            return RateLimitResult.allowed();
        }

        RateLimitProperties.RateLimitRule rule = rateLimitProperties.getRules().get(ruleName);
        if (rule == null) {
            rule = rateLimitProperties.getRules().get(rateLimitProperties.getDefaultRule());
        }

        String key = RATE_LIMIT_PREFIX + ruleName + ":ip:" + ipAddress;

        return executeRateLimitCheck(key, rule);
    }

    public void resetRateLimit(String userId, String ruleName) {
        String key = RATE_LIMIT_PREFIX + ruleName + ":" + userId;
        redisTemplate.delete(key);
    }

    public RateLimitStatus getRateLimitStatus(String userId, String ruleName) {
        RateLimitProperties.RateLimitRule rule = rateLimitProperties.getRules().get(ruleName);
        if (rule == null) {
            rule = rateLimitProperties.getRules().get(rateLimitProperties.getDefaultRule());
        }

        String key = RATE_LIMIT_PREFIX + ruleName + ":" + userId;
        long currentTime = System.currentTimeMillis();
        long windowStartTime = currentTime - (rule.getWindowSeconds() * 1000);

        // Clean up expired entries first
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, windowStartTime);

        Long currentCount = redisTemplate.opsForZSet().count(key, windowStartTime,
                currentTime);
        long remaining = Math.max(0, rule.getMaxRequests() - (currentCount != null ? currentCount : 0));
        long resetTime = currentTime + (rule.getWindowSeconds() * 1000);

        return RateLimitStatus.builder()
                .userId(userId)
                .ruleName(ruleName)
                .limit(rule.getMaxRequests())
                .remaining(remaining)
                .resetTime(resetTime)
                .windowSeconds(rule.getWindowSeconds())
                .description(rule.getDescription())
                .build();
    }

    @Data
    @Builder
    public static class RateLimitResult {
        private final boolean allowed;
        private final int limit;
        private final long remaining;
        private final long resetTime;
        private final String description;
        private final String message;

        public static RateLimitResult allowed() {
            return RateLimitResult.builder()
                    .allowed(true)
                    .build();
        }

        public static RateLimitResult allowed(int limit, long remaining, long resetTime, String description) {
            return RateLimitResult.builder()
                    .allowed(true)
                    .limit(limit)
                    .remaining(remaining)
                    .resetTime(resetTime)
                    .description(description)
                    .build();
        }

        public static RateLimitResult blocked(int limit, long remaining, long resetTime, String description) {
            return RateLimitResult.builder()
                    .allowed(false)
                    .limit(limit)
                    .remaining(remaining)
                    .resetTime(resetTime)
                    .description(description)
                    .message("Rate limit exceeded. Try again later.")
                    .build();
        }
    }

    @Data
    @Builder
    public static class RateLimitStatus {
        private final String userId;
        private final String ruleName;
        private final int limit;
        private final long remaining;
        private final long resetTime;
        private final long windowSeconds;
        private final String description;
    }
}