package com.EIPplatform.service.authentication;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.EIPplatform.annotations.TrackHistoryActions;
import com.EIPplatform.controller.helpers.EmailService;
import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.AuthenticationError;
import com.EIPplatform.exception.errorCategories.SystemError;
import com.EIPplatform.exception.exceptions.AppException;
import com.EIPplatform.model.dto.authentication.AuthenticationResponse;
import com.EIPplatform.model.dto.authentication.IsValidTokenRequest;
import com.EIPplatform.model.dto.authentication.IsValidTokenResponse;
import com.EIPplatform.model.dto.authentication.LoginRequest;
import com.EIPplatform.model.dto.authentication.PasswordChangeVerificationRequest;
import com.EIPplatform.model.dto.authentication.PasswordChangeWithCodeRequest;
import com.EIPplatform.model.dto.authentication.Tokens;
import com.EIPplatform.model.dto.authentication.UserAccountAuthenticationResponse;
import com.EIPplatform.model.dto.authentication.UserRedisSessionData;
import com.EIPplatform.model.entity.user.authentication.InvalidatedToken;
import com.EIPplatform.model.entity.user.authentication.PasswordChangeCode;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.enums.TokenType;
import com.EIPplatform.repository.authentication.InvalidatedTokenRepository;
import com.EIPplatform.repository.authentication.PasswordChangeCodeRepository;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.EIPplatform.utils.GlobalUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationImplementation implements AuthenticationServiceInterface {

    PasswordEncoder passwordEncoder;
    UserAccountRepository userRepository;
    ExceptionFactory exceptionFactory;

    Long MAX_VERIFICATION_REQUEST = 3L;
    InvalidatedTokenRepository invalidatedTokenRepository;
    JWTServiceImplementation JWTService;
    RedisLoginRateLimiter redisLoginRateLimiter;
    PasswordChangeCodeRepository passwordChangeCodeRepository;
    EmailService emailService;
    GlobalUtils globalUtils;

    @Override
    public IsValidTokenResponse checkJWTToken(IsValidTokenRequest request) throws JOSEException, ParseException {
        String token = request.getToken();
        boolean isValid = true;
        UUID userId = null;
        try {
            SignedJWT signedJWT = JWTService.verifyToken(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            userId = UUID.fromString(claimsSet.getSubject());
        } catch (Exception e) {
            isValid = false;

        }

        return IsValidTokenResponse.builder()
                .valid(isValid)
                .userId(userId)
                .build();
    }

    @Override
    @TrackHistoryActions(action = "User just called refresh token", trackParams = {
        ""})
    public AuthenticationResponse refreshToken(HttpServletRequest request) throws Exception {
        String refreshToken = extractCookieValue(request, "refreshToken");
        if (refreshToken == null) {
            throw new AppException(SystemError.FAILED_TO_REFRESH);
        }

        SignedJWT signToken;

        try {
            signToken = JWTService.verifyToken(refreshToken);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(SystemError.FAILED_TO_REFRESH);
        }
        String oldRefreshTokenId = signToken.getJWTClaimsSet().getJWTID();
        saveInvalidatedToken(signToken);

        UUID userId;
        try {
            userId = UUID.fromString(signToken.getJWTClaimsSet().getSubject());
        } catch (IllegalArgumentException e) {
            throw new AppException(SystemError.FAILED_TO_REFRESH);
        }

        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(SystemError.FAILED_TO_REFRESH));

        if (!user.isEnable()) {
            throw new AppException(AuthenticationError.USER_DELETED);
        }

        UserRedisSessionData currentSession;
        try {
            currentSession = JWTService.checkingSession(oldRefreshTokenId, userId);
        } catch (AppException e) {
            throw e;
        }

        String newAccessTokenId = UUID.randomUUID().toString();
        String newRefreshTokenId = UUID.randomUUID().toString();

        try {
            String newAccessToken = JWTService.generateToken(user, TokenType.ACCESS.name(),
                    newAccessTokenId);
            String newRefreshToken = JWTService.generateToken(user, TokenType.REFRESH.name(),
                    newRefreshTokenId);

            JWTService.updateSessionTokens(newAccessTokenId, newRefreshTokenId, currentSession);

            return AuthenticationResponse.builder()
                    .authenticated(true)
                    .user(UserAccountAuthenticationResponse.builder()
                            .userAccountId(user.getUserAccountId())
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .roles(user.getRoles().stream().map(each -> each.getRoleName().name()).toList())
                            .build())
                    .tokens(Tokens.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
                            .build())
                    .build();

        } catch (Exception e) {
            JWTService.invalidateSession(currentSession.getSessionId());
            throw new AppException(SystemError.FAILED_TO_REFRESH);
        }
    }

    //done
    @TrackHistoryActions(action = "User with email {email} logged in", trackParams = {"email"})
    @Override
    public AuthenticationResponse loginAuthenticate(LoginRequest request, HttpServletRequest httpRequest) {

        String email = request.getEmail();

        UserAccount user = userRepository.findUserWithRolesByEmail(email)
                .orElseThrow(() -> exceptionFactory.createCustomException(AuthenticationError.LOGIN_FAILED));

        if (!user.isEnable()) {
            throw exceptionFactory.createCustomException(AuthenticationError.USER_DELETED);
        }

        if (redisLoginRateLimiter.isBlocked(email)) {
            long remainingSeconds = redisLoginRateLimiter.getRemainingBlockTime(email);
            throw exceptionFactory.createCustomException(List.of("blockingTime"), List.of(remainingSeconds),
                    AuthenticationError.USER_BLOCKED);

        }

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            boolean shouldBlock = redisLoginRateLimiter.recordFailedAttempt(email);
            if (shouldBlock) {
                long remainingSeconds = redisLoginRateLimiter.getRemainingBlockTime(email);
                throw exceptionFactory.createCustomException(List.of("blockingTime"), List.of(remainingSeconds),
                        AuthenticationError.USER_BLOCKED);
            }

            throw new AppException(AuthenticationError.LOGIN_FAILED);
        }

        redisLoginRateLimiter.clearAttempts(email);

        Tokens tokens = JWTService.generateTokenPair(user, httpRequest);

        return AuthenticationResponse.builder()
                .authenticated(true)
                .user(UserAccountAuthenticationResponse.builder()
                        .userAccountId(user.getUserAccountId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .roles(user.getRoles().stream().map(each -> each.getRoleName().name()).toList())
                        .build())
                .tokens(tokens)
                .build();
    }

     @TrackHistoryActions(action = "User with email {email} requested password change verification", trackParams = {
            "email" })
    public void passwordChangeVerification(PasswordChangeVerificationRequest request) {

        UserAccount user = userRepository.findUserWithRolesByEmail(request.getEmail())
                .orElseThrow(() -> exceptionFactory.createCustomException(AuthenticationError.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw exceptionFactory.createCustomException(AuthenticationError.LOGIN_FAILED);
        }

        if (!user.isEnable()) {
            throw exceptionFactory.createCustomException(AuthenticationError.USER_DELETED);
        }

        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        Long recentCodes = passwordChangeCodeRepository.countRecentCodesByEmail(request.getEmail(), oneDayAgo);
        if (recentCodes >= MAX_VERIFICATION_REQUEST) {
            throw exceptionFactory.createCustomException(AuthenticationError.TOO_MANY_VERIFICATION_REQUESTS);
        }

        String verificationCode = globalUtils.generateVerificationCode();

        PasswordChangeCode codeEntity = PasswordChangeCode.builder()
                .email(request.getEmail())
                .code(verificationCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .used(false)
                .build();

        passwordChangeCodeRepository.save(codeEntity);

        try {
            emailService.sendPasswordChangeVerificationCode(
                    request.getEmail(),
                    verificationCode,
                    user.getFullName());
        } catch (Exception e) {
            passwordChangeCodeRepository.delete(codeEntity);
            throw exceptionFactory.createCustomException(SystemError.FAILED_TO_SEND_EMAIL);
        }
    }

     public void changePasswordWithCode(PasswordChangeWithCodeRequest request) {

        UserAccount user = userRepository.findUserWithRolesByEmail(request.getEmail()).orElseThrow(() -> exceptionFactory
                .createNotFoundException("User", "Email", request.getEmail(), AuthenticationError.USER_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw exceptionFactory.createCustomException(SystemError.UNAUTHENTICATED);
        }

        UUID authenticatedUserId;

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();
            authenticatedUserId = UUID.fromString(jwt.getSubject());
        } else {
            throw exceptionFactory.createCustomException(SystemError.UNAUTHENTICATED);
        }

        if (!authenticatedUserId.equals(user.getUserAccountId())) {
            throw exceptionFactory.createCustomException(AuthenticationError.FAILED_TO_CHANGE_PASSWORD);
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw exceptionFactory.createCustomException(AuthenticationError.PASSWORD_CONFIRMATION_MISMATCH);
        }

        if (!user.isEnable()) {
            throw exceptionFactory.createCustomException(AuthenticationError.USER_DELETED);
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw exceptionFactory.createCustomException(AuthenticationError.SAME_OLD_PASSWORD);
        }

        PasswordChangeCode codeEntity = passwordChangeCodeRepository
                .findUnusedVerificationCode(request.getEmail(), request.getVerificationCode())
                .orElseThrow(
                        () -> exceptionFactory.createCustomException(AuthenticationError.INVALID_VERIFICATION_CODE));

        if (codeEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            passwordChangeCodeRepository.delete(codeEntity);
            throw exceptionFactory.createCustomException(AuthenticationError.VERIFICATION_CODE_EXPIRED);
        }

        String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedNewPassword);
        userRepository.save(user);

        codeEntity.setUsed(true);
        passwordChangeCodeRepository.save(codeEntity);

        try {
            JWTService.invalidateAllUserSessions(user.getUserAccountId());
        } catch (Exception e) {
            log.info("Failed to invalidate user sessions: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredTokensEveryDay() {
        invalidatedTokenRepository.deleteExpiredTokens(new Date());
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredVerificationCodes() {
        passwordChangeCodeRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    @Override
       public void logout(IsValidTokenRequest request, HttpServletRequest httpRequest) throws JOSEException, ParseException {
        String accessToken = request.getToken();
        String refreshToken = extractCookieValue(httpRequest, "refreshToken");
        if (accessToken != null) {
            try {
                invalidatedTokenForLogout(accessToken);
            } catch (Exception e) {
                log.info("Access token already expired");
            }
        }

        if (refreshToken != null) {
            try {
                invalidatedTokenForLogout(refreshToken);
            } catch (Exception e) {
                log.info("Refresh token already expired");
            }
        }

    }


    @SuppressWarnings("unused")
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

    private void saveInvalidatedToken(SignedJWT signToken) throws ParseException {
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date issueTime = signToken.getJWTClaimsSet().getIssueTime();
        String tokenType = (String) signToken.getJWTClaimsSet().getClaim("type");

        if (invalidatedTokenRepository.existsById(jit)) {
            return;
        }

        invalidatedTokenRepository.save(InvalidatedToken.builder()
                .issueTime(issueTime)
                .id(jit)
                .tokenType(tokenType)
                .build());
    }

    private void invalidatedTokenForLogout(String token) {
        try {
            SignedJWT signedJWT = JWTService.verifyToken(token);
            String tokenId = signedJWT.getJWTClaimsSet().getJWTID();
            saveInvalidatedToken(signedJWT);

            JWTService.logoutByToken(tokenId);

        } catch (Exception e) {
            log.info("Token already expired");
        }
    }

}
