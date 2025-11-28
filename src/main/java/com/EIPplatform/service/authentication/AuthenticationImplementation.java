// package com.EIPplatform.service.authentication;

// import java.text.ParseException;
// import java.util.UUID;

// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;

// import com.EIPplatform.annotations.TrackHistoryActions;
// import com.EIPplatform.exception.ExceptionFactory;
// import com.EIPplatform.exception.errorCategories.AuthenticationError;
// import com.EIPplatform.exception.errorCategories.SystemError;
// import com.EIPplatform.exception.exceptions.AppException;
// import com.EIPplatform.model.dto.authentication.AuthenticationResponse;
// import com.EIPplatform.model.dto.authentication.IsValidTokenRequest;
// import com.EIPplatform.model.dto.authentication.IsValidTokenResponse;
// import com.EIPplatform.model.dto.authentication.LoginRequest;
// import com.EIPplatform.model.dto.authentication.UserAccountAuthenticationResponse;
// import com.EIPplatform.model.dto.authentication.UserRedisSessionData;
// import com.EIPplatform.model.entity.user.authentication.UserAccount;
// import com.EIPplatform.repository.authentication.UserAccountRepository;
// import com.EIPplatform.utils.GlobalUtils;
// import com.nimbusds.jose.JOSEException;
// import com.nimbusds.jwt.SignedJWT;

// import jakarta.servlet.http.Cookie;
// import jakarta.servlet.http.HttpServletRequest;
// import lombok.AccessLevel;
// import lombok.RequiredArgsConstructor;
// import lombok.experimental.FieldDefaults;
// import lombok.extern.slf4j.Slf4j;

// @Service
// @Slf4j
// @RequiredArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// public class AuthenticationImplementation implements AuthenticationServiceInterface {

//     PasswordEncoder passwordEncoder;
//     UserAccountRepository userRepository;
//     ExceptionFactory exceptionFactory;

//     Long MAX_VERIFICATION_REQUEST = 3L;
//     InvalidatedTokenRepository invalidatedTokenRepository;
//     JWTServiceImplementation JWTService;
//     RedisLoginRateLimiter redisLoginRateLimiter;
//     PasswordChangeCodeRepository passwordChangeCodeRepository;
//     EmailService emailService;
//     GlobalUtils globalUtils;

//     @Override
//     public IsValidTokenResponse checkJWTToken(IsValidTokenRequest request) throws JOSEException, ParseException {
//         throw new UnsupportedOperationException("Not supported yet.");
//     }

//     @Override
//     @TrackHistoryActions(action = "User just called refresh token", trackParams = {
//         ""})
//     public AuthenticationResponse refreshToken(HttpServletRequest httpRequest) throws Exception {
//         String refreshToken = extractCookieValue(request, "refreshToken");
//         if (refreshToken == null) {
//             throw new AppException(SystemError.FAILED_TO_REFRESH);
//         }

//         SignedJWT signToken;

//         try {
//             signToken = JWTService.verifyToken(refreshToken);
//         } catch (AppException e) {
//             throw e;
//         } catch (Exception e) {
//             throw new AppException(SystemError.FAILED_TO_REFRESH);
//         }
//         String oldRefreshTokenId = signToken.getJWTClaimsSet().getJWTID();
//         saveInvalidatedToken(signToken);

//         UUID userId;
//         try {
//             userId = UUID.fromString(signToken.getJWTClaimsSet().getSubject());
//         } catch (IllegalArgumentException e) {
//             throw new AppException(SystemError.FAILED_TO_REFRESH);
//         }

//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new AppException(SystemError.FAILED_TO_REFRESH));

//         if (!user.isEnable()) {
//             throw new AppException(AuthenticationError.USER_DELETED);
//         }

//         UserRedisSessionData currentSession;
//         try {
//             currentSession = JWTService.checkingSession(oldRefreshTokenId, userId);
//         } catch (AppException e) {
//             throw e;
//         }

//         String newAccessTokenId = UUID.randomUUID().toString();
//         String newRefreshTokenId = UUID.randomUUID().toString();

//         try {
//             String newAccessToken = JWTService.generateToken(user, TokenType.ACCESS.name(),
//                     newAccessTokenId);
//             String newRefreshToken = JWTService.generateToken(user, TokenType.REFRESH.name(),
//                     newRefreshTokenId);

//             JWTService.updateSessionTokens(newAccessTokenId, newRefreshTokenId, currentSession);

//             return AuthenticationResponse.builder()
//                     .authenticated(true)
//                     .user(UserResponse.builder()
//                             .userId(user.getUserId())
//                             .email(user.getEmail())
//                             .fullName(user.getFullName())
//                             .roles(user.getRoles().stream().map(each -> each.getRoleName().name()).toList())
//                             .build())
//                     .tokens(Tokens.builder()
//                             .accessToken(newAccessToken)
//                             .refreshToken(newRefreshToken)
//                             .build())
//                     .build();

//         } catch (Exception e) {
//             JWTService.invalidateSession(currentSession.getSessionId());
//             throw new AppException(SystemError.FAILED_TO_REFRESH);
//         }
//     }

//     //done
//     @TrackHistoryActions(action = "User with email {email} logged in", trackParams = {"email"})
//     @Override
//     public AuthenticationResponse loginAuthenticate(LoginRequest request, HttpServletRequest httpRequest) {

//         String email = request.getEmail();

//         UserAccount user = userRepository.findUserWithRolesByEmail(email)
//                 .orElseThrow(() -> exceptionFactory.createCustomException(AuthenticationError.LOGIN_FAILED));

//         if (!user.isEnable()) {
//             throw exceptionFactory.createCustomException(AuthenticationError.USER_DELETED);
//         }

//         if (request.getPassword() == null || !request.getPassword().equals(user.getPassword())) {
//             throw exceptionFactory.createCustomException(AuthenticationError.LOGIN_FAILED);
//         }

//         return AuthenticationResponse.builder()
//                 .authenticated(true)
//                 .user(UserAccountAuthenticationResponse.builder()
//                         .userAccountId(user.getUserAccountId())
//                         .email(user.getEmail())
//                         .fullName(user.getFullName())
//                         .roles(user.getRoles().stream().map(each -> each.getRoleName().name()).toList())
//                         .build())
//                 // .tokens(tokens)
//                 .build();
//     }

//     @Override
//     public void logout(IsValidTokenRequest request, HttpServletRequest httpRequest) {
//         String accessToken = request.getToken();
//         String refreshToken = extractCookieValue(httpRequest, "refreshToken");
//         if (accessToken != null) {
//             log.info("Access token logout skipped (not implemented)");
//         }
//         if (refreshToken != null) {
//             log.info("Refresh token logout skipped (not implemented)");
//         }
//     }

//     @SuppressWarnings("unused")
//     private String extractCookieValue(HttpServletRequest request, String cookieName) {
//         if (request.getCookies() != null) {
//             for (Cookie cookie : request.getCookies()) {
//                 if (cookie.getName().equals(cookieName)) {
//                     return cookie.getValue();
//                 }
//             }
//         }
//         return null;
//     }
// }
