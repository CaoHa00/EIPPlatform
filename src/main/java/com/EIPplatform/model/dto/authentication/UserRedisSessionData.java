package com.EIPplatform.model.dto.authentication;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRedisSessionData {
    String sessionId;
    UUID userId;
    String accessTokenId;
    String refreshTokenId;
    LocalDateTime createdAt;
    LocalDateTime expiresAt;
    String ipAddress;
    String userAgent;
    Map<String, Object> metadata;
}