package com.EIPplatform.controller.authentication;

import java.text.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EIPplatform.annotations.RateLimit;
import com.EIPplatform.model.dto.api.ApiResponse;
import com.EIPplatform.model.dto.authentication.IsValidTokenRequest;
import com.EIPplatform.model.dto.authentication.IsValidTokenResponse;
import com.EIPplatform.service.authentication.AuthenticationServiceInterface;
import com.nimbusds.jose.JOSEException;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.*;
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
        
}
