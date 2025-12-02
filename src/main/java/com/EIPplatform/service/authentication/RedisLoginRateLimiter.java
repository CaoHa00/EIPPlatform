package com.EIPplatform.service.authentication;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisLoginRateLimiter {
    RedisTemplate<String, String> redisTemplate;

    static final String LOGIN_ATTEMPTS_PREFIX = "login_attempts:";
    static final String BLOCKED_USER_PREFIX = "blocked_user:";
    static final int MAX_ATTEMPTS = 3;
    static final Duration BLOCK_DURATION = Duration.ofMinutes(5);

    public boolean isBlocked(String email) {
        String blockKey = BLOCKED_USER_PREFIX + email;
        return redisTemplate.hasKey(blockKey);
    }

    public long getRemainingBlockTime(String email) {
        String blockKey = BLOCKED_USER_PREFIX + email;
        Long ttl = redisTemplate.getExpire(blockKey, TimeUnit.SECONDS);
        return ttl != null && ttl > 0 ? ttl : 0;
    }

    public boolean recordFailedAttempt(String email) {
        String attemptKey = LOGIN_ATTEMPTS_PREFIX + email;

        Long attempts = redisTemplate.opsForValue().increment(attemptKey);

        if (attempts == 1) {
            redisTemplate.expire(attemptKey, BLOCK_DURATION);
        }

        if (attempts >= MAX_ATTEMPTS) {
            blockUser(email);
            return true;
        }

        return false;
    }

    private void blockUser(String email) {
        String attemptKey = LOGIN_ATTEMPTS_PREFIX + email;
        String blockKey = BLOCKED_USER_PREFIX + email;

        redisTemplate.opsForValue().set(blockKey, "blocked", BLOCK_DURATION);

        redisTemplate.delete(attemptKey);
    }

    public void clearAttempts(String email) {
        String attemptKey = LOGIN_ATTEMPTS_PREFIX + email;
        String blockKey = BLOCKED_USER_PREFIX + email;

        redisTemplate.delete(attemptKey);
        redisTemplate.delete(blockKey);
    }

    public int getCurrentAttempts(String email) {
        String attemptKey = LOGIN_ATTEMPTS_PREFIX + email;
        String attempts = redisTemplate.opsForValue().get(attemptKey);
        return attempts != null ? Integer.parseInt(attempts) : 0;
    }
}