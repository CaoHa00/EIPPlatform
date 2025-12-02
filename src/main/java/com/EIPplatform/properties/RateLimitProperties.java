package com.EIPplatform.properties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@ConfigurationProperties(prefix = "app.rate-limit")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateLimitProperties {
    Map<String, RateLimitRule> rules = new HashMap<>();
    boolean enabled = true;
    String defaultRule = "default";

    public RateLimitProperties() {
        rules.put("auth",
                new RateLimitRule(5, Duration.ofMinutes(1), "Authentication endpoints - Brute force protection"));
        rules.put("session",
                new RateLimitRule(10, Duration.ofMinutes(1), "Session and token operations - Security protection"));
        rules.put("default",
                new RateLimitRule(200, Duration.ofHours(1), "General API access - Conservative security limit"));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RateLimitRule {
        private int maxRequests;
        private Duration window;
        private String description;

        public long getWindowSeconds() {
            return window.toSeconds();
        }
    }
}