package com.EIPplatform.service.authentication;

import java.text.ParseException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.EIPplatform.annotations.TrackHistoryActions;
import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.AuthenticationError;
import com.EIPplatform.exception.exceptions.AppException;
import com.EIPplatform.model.dto.authentication.AuthenticationResponse;
import com.EIPplatform.model.dto.authentication.IsValidTokenRequest;
import com.EIPplatform.model.dto.authentication.IsValidTokenResponse;
import com.EIPplatform.model.dto.authentication.LoginRequest;
import com.EIPplatform.model.dto.authentication.UserAccountAuthenticationResponse;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import com.nimbusds.jose.JOSEException;

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
    // Long MAX_VERIFICATION_REQUEST = 3L;
    // InvalidatedTokenRepository invalidatedTokenRepository;
    // JWTServiceImplementation JWTService;
    // RedisLoginRateLimiter redisLoginRateLimiter;
    // PasswordChangeCodeRepository passwordChangeCodeRepository;
    // EmailService emailService;
    // GlobalUtils globalUtils;
    @Override
    public IsValidTokenResponse checkJWTToken(IsValidTokenRequest request) throws JOSEException, ParseException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest httpRequest) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

        // done
    @TrackHistoryActions(action = "User with email {email} loginned", trackParams = {
            "email" })
    public AuthenticationResponse loginAuthenticate(LoginRequest request, HttpServletRequest httpRequest) {

        String email = request.getEmail();

        UserAccount user = userRepository.findUserWithRolesByEmail(email)
                .orElseThrow(() -> exceptionFactory.createCustomException(AuthenticationError.LOGIN_FAILED));

        if (!user.isEnable()) {
            throw exceptionFactory.createCustomException(AuthenticationError.USER_DELETED);
        }
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        //Tokens tokens = JWTService.generateTokenPair(user, httpRequest);
        if (!authenticated) {
            throw new AppException(AuthenticationError.LOGIN_FAILED);
        }
        return AuthenticationResponse.builder()
                .authenticated(true)
                .user(UserAccountAuthenticationResponse.builder()
                        .userAccountId(user.getUserAccountId())
                        .email(user.getEmail())
                        // .fullName(user.getFullName())
                        .roles(user.getRoles().stream().map(each -> each.getRoleName().name()).toList())
                        .build())
                // .tokens(tokens)
                .build();
    }

    // @Override
    // public void logout(IsValidTokenRequest request, HttpServletRequest httpRequest) throws JOSEException, ParseException {
    //     String accessToken = request.getToken();
    //     String refreshToken = extractCookieValue(httpRequest, "refreshToken");
    //     if (accessToken != null) {
    //         try {
    //             invalidatedTokenForLogout(accessToken);
    //         } catch (Exception e) {
    //             log.info("Access token already expired");
    //         }
    //     }

    //     if (refreshToken != null) {
    //         try {
    //             invalidatedTokenForLogout(refreshToken);
    //         } catch (Exception e) {
    //             log.info("Refresh token already expired");
    //         }
    //     }

    // }

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
}
