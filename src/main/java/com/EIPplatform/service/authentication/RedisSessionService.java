package com.EIPplatform.service.authentication;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.EIPplatform.exception.errorCategories.SystemError;
import com.EIPplatform.exception.exceptions.AppException;
import com.EIPplatform.model.dto.authentication.UserRedisSessionData;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisSessionService {

    RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper;

    static final String USER_SESSION_PREFIX = "session:"; // session:sessionId -> SessionData
    static final String TOKEN_SESSION_PREFIX = "token:"; // token:tokenId -> sessionId
    static final String ACTIVE_USER_PREFIX = "active_user:"; // active_user:userId -> sessionId
    static final String USER_SESSIONS_SET_PREFIX = "user_sessions:"; // user_sessions:userId -> Set<sessionId>

    private UserRedisSessionData convertToSessionData(Object data) {
        if (data == null) {
            return null;
        }

        try {
            if (data instanceof UserRedisSessionData) {
                return (UserRedisSessionData) data;
            } else if (data instanceof LinkedHashMap) {
                return objectMapper.convertValue(data, UserRedisSessionData.class);
            } else {
                log.warn("Unexpected data type from Redis: {}", data.getClass().getSimpleName());
                return null;
            }
        } catch (Exception e) {
            log.error("Error converting Redis data to UserRedisSessionData", e);
            return null;
        }
    }

    public void createSession(UserRedisSessionData sessionData) {
        long ttlSeconds = Duration.between(LocalDateTime.now(), sessionData.getExpiresAt()).toSeconds();
        invalidateAllUserSessions(sessionData.getUserId());
        storeSessionData(sessionData, ttlSeconds);
    }

    public void logoutByToken(String accessTokenId) {
        try {
            String sessionId = (String) redisTemplate.opsForValue().get(TOKEN_SESSION_PREFIX + accessTokenId);

            if (sessionId == null) {
                return;
            }

            Object sessionObj = redisTemplate.opsForValue().get(USER_SESSION_PREFIX + sessionId);
            UserRedisSessionData session = convertToSessionData(sessionObj);

            if (session == null) {
                return;
            }

            List<String> keysToDelete = Arrays.asList(
                    USER_SESSION_PREFIX + sessionId,
                    TOKEN_SESSION_PREFIX + session.getAccessTokenId(),
                    TOKEN_SESSION_PREFIX + session.getRefreshTokenId(),
                    ACTIVE_USER_PREFIX + session.getUserId());

            redisTemplate.delete(keysToDelete);

        } catch (Exception e) {
            log.error("Error during logout by access token", e);
        }
    }

    /**
     * 🔥 LOGOUT BY USER ID
     * Force logout all sessions for a specific user (admin function)
     * Scenario : Password change or is banned,...
     */
    public void logoutAllUserSessions(UUID userId) {
        try {
            invalidateAllUserSessions(userId);
        } catch (Exception e) {
            log.error("Error during logout all sessions for user: {}", userId, e);
        }
    }

    public boolean isSessionValid(String tokenId) {
        try {
            String sessionId = (String) redisTemplate.opsForValue().get(TOKEN_SESSION_PREFIX + tokenId);
            if (sessionId == null) {
                return false;
            }

            // Fixed: Use safe conversion method
            Object sessionObj = redisTemplate.opsForValue().get(USER_SESSION_PREFIX + sessionId);
            UserRedisSessionData session = convertToSessionData(sessionObj);

            if (session == null) {
                return false;
            }

            boolean isValid = session.getExpiresAt().isAfter(LocalDateTime.now());
            return isValid;

        } catch (Exception e) {
            log.error("Error validating session for token: {}", tokenId, e);
            return false;
        }
    }

    public Optional<UserRedisSessionData> getSessionByToken(String tokenId) {
        try {
            String sessionId = (String) redisTemplate.opsForValue().get(TOKEN_SESSION_PREFIX + tokenId);
            if (sessionId == null) {
                return Optional.empty();
            }

            // Fixed: Use safe conversion method
            Object sessionObj = redisTemplate.opsForValue().get(USER_SESSION_PREFIX + sessionId);
            UserRedisSessionData session = convertToSessionData(sessionObj);

            return Optional.ofNullable(session);

        } catch (Exception e) {
            log.error("Error getting session by token: {}", tokenId, e);
            return Optional.empty();
        }
    }

    public void invalidateAllUserSessions(UUID userId) {
        try {
            String activeSessionId = (String) redisTemplate.opsForValue().get(ACTIVE_USER_PREFIX + userId);

            if (activeSessionId != null) {
                // Fixed: Use safe conversion method
                Object sessionObj = redisTemplate.opsForValue().get(USER_SESSION_PREFIX + activeSessionId);
                UserRedisSessionData currentSession = convertToSessionData(sessionObj);

                if (currentSession != null) {
                    List<String> keysToDelete = Arrays.asList(
                            USER_SESSION_PREFIX + activeSessionId,
                            TOKEN_SESSION_PREFIX + currentSession.getAccessTokenId(),
                            TOKEN_SESSION_PREFIX + currentSession.getRefreshTokenId(),
                            ACTIVE_USER_PREFIX + userId);

                    redisTemplate.delete(keysToDelete);
                }
            }

        } catch (Exception e) {
            log.error("Error invalidating sessions for user: {}", userId, e);
        }
    }

    public void invalidateSession(String sessionId) {
        try {
            // Fixed: Use safe conversion method
            Object sessionObj = redisTemplate.opsForValue().get(USER_SESSION_PREFIX + sessionId);
            UserRedisSessionData session = convertToSessionData(sessionObj);

            if (session != null) {
                List<String> keysToDelete = Arrays.asList(
                        USER_SESSION_PREFIX + sessionId,
                        TOKEN_SESSION_PREFIX + session.getAccessTokenId(),
                        TOKEN_SESSION_PREFIX + session.getRefreshTokenId(),
                        ACTIVE_USER_PREFIX + session.getUserId());

                redisTemplate.delete(keysToDelete);
            }

        } catch (Exception e) {
            log.error("Error invalidating session: {}", sessionId, e);
        }
    }

    private void storeSessionData(UserRedisSessionData sessionData, long ttlSeconds) {
        try {
            String sessionKey = USER_SESSION_PREFIX + sessionData.getSessionId();
            String userActiveKey = ACTIVE_USER_PREFIX + sessionData.getUserId();
            String accessTokenKey = TOKEN_SESSION_PREFIX + sessionData.getAccessTokenId();
            String refreshTokenKey = TOKEN_SESSION_PREFIX + sessionData.getRefreshTokenId();

            Duration ttl = Duration.ofSeconds(ttlSeconds);

            redisTemplate.opsForValue().set(sessionKey, sessionData, ttl);
            redisTemplate.opsForValue().set(userActiveKey, sessionData.getSessionId(), ttl);
            redisTemplate.opsForValue().set(accessTokenKey, sessionData.getSessionId(), ttl);
            redisTemplate.opsForValue().set(refreshTokenKey, sessionData.getSessionId(), ttl);

        } catch (Exception e) {
            log.error("Error storing session data", e);
            throw new RuntimeException("Failed to store session data", e);
        }
    }

    public Map<String, Object> getSessionStats() {
        try {
            Set<String> sessionKeys = redisTemplate.keys(USER_SESSION_PREFIX + "*");
            Set<String> tokenKeys = redisTemplate.keys(TOKEN_SESSION_PREFIX + "*");
            Set<String> userKeys = redisTemplate.keys(ACTIVE_USER_PREFIX + "*");

            return Map.of(
                    "activeSessions", sessionKeys != null ? sessionKeys.size() : 0,
                    "tokenMappings", tokenKeys != null ? tokenKeys.size() : 0,
                    "activeUsers", userKeys != null ? userKeys.size() : 0,
                    "timestamp", LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error getting session stats", e);
            return Map.of("error", "Failed to get stats");
        }
    }

    public void updateSessionTokens(String sessionId, String newAccessTokenId, String newRefreshTokenId,
            LocalDateTime newExpiresAt) {
        try {
            Object sessionObj = redisTemplate.opsForValue().get(USER_SESSION_PREFIX + sessionId);
            UserRedisSessionData existingSession = convertToSessionData(sessionObj);

            if (existingSession == null) {
                throw new AppException(SystemError.FAILED_TO_REFRESH);
            }

            redisTemplate.delete(TOKEN_SESSION_PREFIX + existingSession.getAccessTokenId());
            redisTemplate.delete(TOKEN_SESSION_PREFIX + existingSession.getRefreshTokenId());

            existingSession.setAccessTokenId(newAccessTokenId);
            existingSession.setRefreshTokenId(newRefreshTokenId);
            existingSession.setExpiresAt(newExpiresAt);

            long ttlSeconds = Duration.between(LocalDateTime.now(), newExpiresAt).toSeconds();
            Duration ttl = Duration.ofSeconds(ttlSeconds);

            String sessionKey = USER_SESSION_PREFIX + sessionId;
            String userActiveKey = ACTIVE_USER_PREFIX + existingSession.getUserId();
            String newAccessTokenKey = TOKEN_SESSION_PREFIX + newAccessTokenId;
            String newRefreshTokenKey = TOKEN_SESSION_PREFIX + newRefreshTokenId;

            redisTemplate.opsForValue().set(sessionKey, existingSession, ttl);
            redisTemplate.opsForValue().set(userActiveKey, sessionId, ttl);
            redisTemplate.opsForValue().set(newAccessTokenKey, sessionId, ttl);
            redisTemplate.opsForValue().set(newRefreshTokenKey, sessionId, ttl);

        } catch (Exception e) {
            log.error("Error updating session tokens for sessionId: {}", sessionId, e);
            throw new RuntimeException("Failed to update session tokens for refresh token", e);
        }
    }

    public Optional<UserRedisSessionData> getSessionByTokenId(String tokenId) {
        try {
            String sessionId = (String) redisTemplate.opsForValue().get(TOKEN_SESSION_PREFIX + tokenId);
            if (sessionId == null) {
                return Optional.empty();
            }

            // Fixed: Use safe conversion method
            Object sessionObj = redisTemplate.opsForValue().get(USER_SESSION_PREFIX + sessionId);
            UserRedisSessionData session = convertToSessionData(sessionObj);

            return Optional.ofNullable(session);

        } catch (Exception e) {
            log.error("Error getting session by tokenId: {}", tokenId, e);
            return Optional.empty();
        }
    }
}