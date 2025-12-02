package com.EIPplatform.controller.authentication;

import java.text.ParseException;
import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.annotations.RateLimit;
import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.authentication.AuthenticationResponse;
import com.EIPplatform.model.dto.authentication.IsValidTokenRequest;
import com.EIPplatform.model.dto.authentication.IsValidTokenResponse;
import com.EIPplatform.model.dto.authentication.LoginRequest;
import com.EIPplatform.model.dto.authentication.PasswordChangeVerificationRequest;
import com.EIPplatform.model.dto.authentication.PasswordChangeWithCodeRequest;
import com.EIPplatform.service.authentication.AuthenticationServiceInterface;
import com.nimbusds.jose.JOSEException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

  
        AuthenticationServiceInterface authenticationService;

        @PostMapping("/login")
        @RateLimit(value = "auth", byIp = true)
        ApiResponse<AuthenticationResponse> loginAuthentication(@Valid @RequestBody LoginRequest request,
                        HttpServletRequest httpRequest,
                        HttpServletResponse response) {

                var result = authenticationService.loginAuthenticate(request, httpRequest);

                response.addHeader("Set-Cookie", String.format(
                                "refreshToken=%s; Path=/; HttpOnly; Max-Age=%d; SameSite=Lax",
                                result.getTokens().getRefreshToken(), 24 * 60 * 60));

                ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", result.getTokens().getRefreshToken())
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofSeconds(24 * 60 * 60))
                                .sameSite("Lax")
                                .build();

                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

                String rolesValue = String.join(",", result.getUser().getRoles());
                ResponseCookie rolesCookie = ResponseCookie.from("userRoles", rolesValue)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofHours(1))
                                .sameSite("Lax")
                                .build();

                response.addHeader(HttpHeaders.SET_COOKIE, rolesCookie.toString());

                result.getTokens().setRefreshToken(null);

                return ApiResponse.<AuthenticationResponse>builder()
                                .message("Login Successfully!")
                                .result(result)
                                .build();
        }

        @PostMapping("/checkJWTToken")
        @RateLimit(value = "session")
        ApiResponse<IsValidTokenResponse> checkJWTToken(@RequestBody IsValidTokenRequest request)
                        throws JOSEException, ParseException {
                var result = authenticationService.checkJWTToken(request);

                return ApiResponse.<IsValidTokenResponse>builder()
                                .message("Check Successfully!")
                                .result(result)
                                .build();
        }

        @PostMapping("/refresh")
        @RateLimit(value = "session")
        ApiResponse<AuthenticationResponse> refreshToken(HttpServletRequest request, HttpServletResponse response)
                        throws Exception {
                var result = authenticationService.refreshToken(request);

                if (result.getTokens().getRefreshToken() != null) {
                        ResponseCookie refreshCookie = ResponseCookie
                                        .from("refreshToken", result.getTokens().getRefreshToken())
                                        .httpOnly(true)
                                        .secure(true)
                                        .path("/")
                                        .maxAge(Duration.ofSeconds(24 * 60 * 60))
                                        .sameSite("Lax")
                                        .build();

                        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
                        result.getTokens().setRefreshToken(null);
                }

                String rolesValue = String.join(",", result.getUser().getRoles());
                ResponseCookie rolesCookie = ResponseCookie.from("userRoles", rolesValue)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(Duration.ofHours(1))
                                .sameSite("Lax")
                                .build();

                response.addHeader(HttpHeaders.SET_COOKIE, rolesCookie.toString());

                return ApiResponse.<AuthenticationResponse>builder()
                                .result(result)
                                .build();
        }

        @PostMapping("/logout")
        ApiResponse<Void> logout(@RequestBody IsValidTokenRequest request, HttpServletRequest httpRequest,
                        HttpServletResponse response)
                        throws JOSEException, ParseException {

                authenticationService.logout(request, httpRequest);

                ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("Lax")
                                .build();

                response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
                return ApiResponse.<Void>builder()
                                .build();
        }

        @PostMapping("/changePasswordVerification")
        @RateLimit(value = "auth")
        ApiResponse<Void> changePasswordVerification(@Valid @RequestBody PasswordChangeVerificationRequest request) {
                authenticationService.passwordChangeVerification(request);

                return ApiResponse.<Void>builder()
                                .message("Send verification code to email successfully!")
                                .build();
        }

        @PostMapping("/changePasswordCode")
        @RateLimit(value = "auth")
        ApiResponse<Void> changePasswordCode(@Valid @RequestBody PasswordChangeWithCodeRequest request) {
                authenticationService.changePasswordWithCode(request);

                return ApiResponse.<Void>builder()
                                .message("Change password successfully!")
                                .build();
        }
}
