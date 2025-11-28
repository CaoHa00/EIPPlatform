// package com.EIPplatform.service.authentication;

// import java.time.LocalDateTime;
// import java.util.Date;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.Set;
// import java.util.UUID;

// import org.springframework.stereotype.Component;
// import org.springframework.util.CollectionUtils;

// import com.EIPplatform.exception.errorCategories.SystemError;
// import com.EIPplatform.exception.exceptions.AppException;
// import com.nimbusds.jose.JWSAlgorithm;
// import com.nimbusds.jose.JWSHeader;
// import com.nimbusds.jose.JWSObject;
// import com.nimbusds.jose.JWSVerifier;
// import com.nimbusds.jose.crypto.MACSigner;
// import com.nimbusds.jose.crypto.MACVerifier;
// import com.nimbusds.jwt.JWTClaimsSet;
// import com.nimbusds.jwt.SignedJWT;

// import org.springframework.beans.factory.annotation.Value;

// import com.EIPplatform.model.entity.user.authentication.Role;
// import com.EIPplatform.model.entity.user.authentication.UserAccount;

// import jakarta.servlet.http.HttpServletRequest;
// import lombok.*;
// import lombok.experimental.FieldDefaults;
// import lombok.experimental.NonFinal;
// @Component
// @RequiredArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// public class JWTServiceImplementation {

//     @NonFinal
//     @Value("${jwt.accessSignerKey}")
//     String ACCESS_SIGNER_KEY;

//     @NonFinal
//     @Value("${jwt.refreshSignerKey}")
//     String REFRESH_SIGNER_KEY;

//     @NonFinal
//     @Value("${jwt.accessToken}")
//     long ACCESS_TOKEN_TIME;

//     @NonFinal
//     @Value("${jwt.refreshToken}")
//     long REFRESH_TOKEN_TIME;

//     String ISSUER = "EIU-IIC4.0-EIP";
//     String AUDIENCE = "EIU-IIC4.0-eip-application";

//     InvalidatedTokenRepository invalidatedTokenRepository;
//     RedisSessionService redisSessionService;
//     AESUtils aesUtils;

//     public SignedJWT verifyToken(String token) throws Exception {
//         String decryptedToken = safeDecrypt(token);
//         SignedJWT signedJWT = SignedJWT.parse(decryptedToken);
//         JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
//         String tokenType = (String) claims.getClaim("type");

//         if (tokenType == null ||
//                 (!tokenType.equals(TokenType.REFRESH.name()) && !tokenType.equals(TokenType.ACCESS.name()))) {
//             throw new AppException(SystemError.UNAUTHENTICATED);
//         }

//         if (!ISSUER.equals(claims.getIssuer())) {
//             throw new AppException(SystemError.UNAUTHENTICATED);
//         }

//         if (claims.getAudience() == null || !claims.getAudience().contains(AUDIENCE)) {
//             throw new AppException(SystemError.UNAUTHENTICATED);
//         }

//         Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

//         String signerKey = tokenType.equals(TokenType.REFRESH.name()) ? REFRESH_SIGNER_KEY : ACCESS_SIGNER_KEY;
//         JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
//         boolean verified = signedJWT.verify(verifier);
//         if (!(verified && expiryTime.after(new Date()))) {
//             if (tokenType.equals(TokenType.REFRESH.name())) {
//                 throw new AppException(SystemError.FAILED_TO_REFRESH);
//             }
//             throw new AppException(SystemError.UNAUTHENTICATED);
//         }

//         if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
//             if (tokenType.equals(TokenType.REFRESH.name())) {
//                 throw new AppException(SystemError.FAILED_TO_REFRESH);
//             }
//             throw new AppException(SystemError.UNAUTHENTICATED);
//         }

//         String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
//         if (!redisSessionService.isSessionValid(jwtId)) {
//             if (tokenType.equals(TokenType.REFRESH.name())) {
//                 throw new AppException(SystemError.FAILED_TO_REFRESH);
//             }
//             throw new AppException(SystemError.SESSION_INVALID);
//         }
//         return signedJWT;
//     }

//     public UserRedisSessionData checkingSession(String oldRefreshTokenId, UUID userId) {
//         Optional<UserRedisSessionData> sessionOpt = redisSessionService.getSessionByTokenId(oldRefreshTokenId);
//         if (sessionOpt.isEmpty()) {
//             throw new AppException(SystemError.FAILED_TO_REFRESH);
//         }

//         UserRedisSessionData currentSession = sessionOpt.get();

//         if (!currentSession.getUserId().equals(userId)) {
//             throw new AppException(SystemError.FAILED_TO_REFRESH);
//         }

//         return currentSession;
//     }

//     public void updateSessionTokens(String newAccessTokenId, String newRefreshTokenId,
//             UserRedisSessionData currentSession) {
//         LocalDateTime newExpiresAt = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_TIME);

//         redisSessionService.updateSessionTokens(
//                 currentSession.getSessionId(),
//                 newAccessTokenId,
//                 newRefreshTokenId,
//                 newExpiresAt);
//     }

//     public void logoutByToken(String tokenId) {
//         redisSessionService.logoutByToken(tokenId);
//     }

//     public void invalidateSession(String sessionId) {
//         redisSessionService.invalidateSession(sessionId);
//     }

//     public void invalidateAllUserSessions(UUID userId) {
//         redisSessionService.logoutAllUserSessions(userId);
//     }

//     public Tokens generateTokenPair(UserAccount user, HttpServletRequest request) {

//         String accessTokenId = UUID.randomUUID().toString();
//         String refreshTokenId = UUID.randomUUID().toString();
//         String sessionId = UUID.randomUUID().toString();

//         String accessToken = generateToken(user, TokenType.ACCESS.name(), accessTokenId);
//         String refreshToken = generateToken(user, TokenType.REFRESH.name(), refreshTokenId);

//         UserRedisSessionData sessionData = UserRedisSessionData.builder()
//                 .sessionId(sessionId)
//                 .userId(user.getUserId())
//                 .accessTokenId(accessTokenId)
//                 .refreshTokenId(refreshTokenId)
//                 .createdAt(LocalDateTime.now())
//                 .expiresAt(LocalDateTime.now().plusSeconds(REFRESH_TOKEN_TIME))
//                 .ipAddress(getClientIp(request))
//                 .userAgent(request.getHeader("User-Agent"))
//                 .metadata(Map.of(
//                         "loginMethod", "password",
//                         "userAgent", Optional.ofNullable(request.getHeader("User-Agent")).orElse("unknown")))
//                 .build();

//         redisSessionService.createSession(sessionData);

//         return Tokens.builder()
//                 .accessToken(accessToken)
//                 .refreshToken(refreshToken)
//                 .build();
//     }

//     public String generateToken(UserAccount user, String type, String jwtId) {
//         JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

//         Date now = new Date();

//         JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
//                 .subject(user.getUserAccountId().toString())
//                 .issuer(ISSUER)
//                 .audience(AUDIENCE)
//                 .issueTime(now)
//                 .expirationTime(Date.from(now.toInstant()
//                         .plusSeconds(type.equals(TokenType.ACCESS.name()) ? ACCESS_TOKEN_TIME : REFRESH_TOKEN_TIME)))
//                 .jwtID(jwtId)
//                 .claim("roles", buildRoles(user))
//                 .claim("fullName", user.getFullName())
//                 .claim("type", type)
//                 .build();

//         Payload payload = new Payload(jwtClaimsSet.toJSONObject());
//         JWSObject jwsObject = new JWSObject(header, payload);
//         String signerKey = type.equals(TokenType.REFRESH.name()) ? REFRESH_SIGNER_KEY : ACCESS_SIGNER_KEY;

//         try {
//             jwsObject.sign(new MACSigner(signerKey.getBytes()));
//             return aesUtils.encrypt(jwsObject.serialize());
//         } catch (Exception e) {
//             throw new RuntimeException("Error signing JWT", e);
//         }
//     }

//     private List<String> buildRoles(UserAccount user) {
//         Set<Role> roles = user.getRoles();
//         if (CollectionUtils.isEmpty(roles))
//             return List.of();

//         return roles.stream()
//                 .map(role -> role.getRoleName().name())
//                 .toList();
//     }

//     private String safeDecrypt(String encryptedToken) {
//         try {

//             return aesUtils.decrypt(encryptedToken);
//         } catch (Exception e) {

//             throw new AppException(SystemError.UNAUTHENTICATED);
//         }
//     }

//     private String getClientIp(HttpServletRequest request) {
//         String xForwardedFor = request.getHeader("X-Forwarded-For");
//         if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
//             return xForwardedFor.split(",")[0].trim();
//         }

//         String xRealIp = request.getHeader("X-Real-IP");
//         if (xRealIp != null && !xRealIp.isEmpty()) {
//             return xRealIp;
//         }

//         return request.getRemoteAddr();
//     }
// }
