package com.EIPplatform.configuration.redis;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.EIPplatform.annotations.RateLimit;
import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.service.authentication.JWTServiceImplementation;
import com.EIPplatform.service.authentication.RedisRateLimitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimitInterceptor implements HandlerInterceptor {

    RedisRateLimitService rateLimitService;
    ObjectMapper objectMapper;
    JWTServiceImplementation jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RateLimit rateLimitAnnotation = getRateLimitAnnotation(handlerMethod);
        if (rateLimitAnnotation == null) {
            return true;
        }

        String ruleName = rateLimitAnnotation.value();
        String userId = extractUserIdFromSecurityContext();
        String ipAddress = getClientIp(request);

        if (userId == null) {
            userId = extractUserIdFromCookies(request);
        }
        if (userId != null) {
            RedisRateLimitService.RateLimitResult userResult = rateLimitService.checkRateLimit(userId, ruleName);

            if (!userResult.isAllowed()) {
                handleRateLimitExceeded(response, userResult, rateLimitAnnotation.message());
                return false;
            }

            addRateLimitHeaders(response, userResult, "user");
        }
        if (rateLimitAnnotation.byIp() && ipAddress != null) {
            RedisRateLimitService.RateLimitResult ipResult = rateLimitService.checkRateLimitByIp(ipAddress, ruleName);

            if (!ipResult.isAllowed()) {
                handleRateLimitExceeded(response, ipResult, rateLimitAnnotation.message());
                return false;
            }
            addRateLimitHeaders(response, ipResult, "ip");
        }

        return true;
    }

    private RateLimit getRateLimitAnnotation(HandlerMethod handlerMethod) {
        RateLimit methodAnnotation = handlerMethod.getMethodAnnotation(RateLimit.class);
        if (methodAnnotation != null) {
            return methodAnnotation;
        }

        return handlerMethod.getBeanType().getAnnotation(RateLimit.class);
    }

    private String extractUserIdFromSecurityContext() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            if (authentication instanceof AnonymousAuthenticationToken) {
                return null;
            }
            Object principal = authentication.getPrincipal();

            if (principal instanceof Jwt) {
                return ((Jwt) principal).getSubject();
            } else if (principal instanceof String) {
                return (String) principal;
            }

            return null;

        } catch (Exception e) {
            log.debug("Could not extract user ID from SecurityContext: {}", e.getMessage());
            return null;
        }
    }

    private void handleRateLimitExceeded(HttpServletResponse response,
            RedisRateLimitService.RateLimitResult result,
            String customMessage) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        addRateLimitHeaders(response, result, "");

        String message = !customMessage.isEmpty() ? customMessage
                : (result.getMessage() != null ? result.getMessage() : "Rate limit exceeded");

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(HttpStatus.TOO_MANY_REQUESTS.value())
                .message(message)
                .result(Map.of(
                        "limit", result.getLimit(),
                        "remaining", result.getRemaining(),
                        "resetTime", result.getResetTime(),
                        "resetTimeFormatted", formatResetTime(result.getResetTime())))
                .build();

        String json = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
    }

    private void addRateLimitHeaders(HttpServletResponse response,
            RedisRateLimitService.RateLimitResult result,
            String prefix) {
        if (result.getLimit() > 0) {
            String headerPrefix = prefix.isEmpty() ? "X-RateLimit-" : "X-RateLimit-" + prefix + "-";

            response.setHeader(headerPrefix + "Limit", String.valueOf(result.getLimit()));
            response.setHeader(headerPrefix + "Remaining", String.valueOf(result.getRemaining()));
            response.setHeader(headerPrefix + "Reset", String.valueOf(result.getResetTime()));
        }
    }

    private String formatResetTime(long resetTimeMillis) {
        try {
            LocalDateTime resetDateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(resetTimeMillis),
                    ZoneId.of("Asia/Ho_Chi_Minh"));
            return resetDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            return "Unknown";
        }
    }

    private String extractUserIdFromCookies(HttpServletRequest request) {
        try {
            String refreshToken = extractCookieValue(request, "refreshToken");

            if (refreshToken == null || refreshToken.isEmpty()) {
                return null;
            }

            SignedJWT signedJWT = jwtService.verifyToken(refreshToken);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            return claimsSet.getSubject();
        } catch (Exception e) {
            log.debug("Could not extract user ID from refresh token cookie: {}",
                    e.getMessage());
            return null;
        }
    }

    private String extractCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}