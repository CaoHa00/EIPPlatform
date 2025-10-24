package com.EIPplatform.controller.authentication;

import java.text.ParseException;

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
import com.EIPplatform.model.dto.authentication.UserAccountAuthenticationResponse;
import com.EIPplatform.service.authentication.AuthenticationServiceInterface;
import com.nimbusds.jose.JOSEException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationServiceInterface authenticationService;

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

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        request.getSession().invalidate();
        ResponseCookie deleteSessionCookie = ResponseCookie.from("JSESSIONID", "")
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteSessionCookie.toString());
        return ApiResponse.<Void>builder()
                .message("Logout successfully!")
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<UserAccountAuthenticationResponse> loginAuthentication(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        AuthenticationResponse authResult = authenticationService.loginAuthenticate(request, httpRequest);

        // ✅ Lưu user vào session (đăng nhập thành công)
        httpRequest.getSession(true).setAttribute("loggedUser", authResult.getUser());

        return ApiResponse.<UserAccountAuthenticationResponse>builder()
                .message("Login successfully!")
                .result(authResult.getUser())
                .build();
    }
}
