//package com.EIPplatform.service.authentication;
//
//import java.text.ParseException;
//import com.EIPplatform.model.dto.authentication.IsValidTokenRequest;
//import com.EIPplatform.model.dto.authentication.IsValidTokenResponse;
//
//import com.EIPplatform.model.dto.authentication.LoginRequest;
//import com.nimbusds.jose.JOSEException;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//public interface AuthenticationServiceInterface {
//
//    IsValidTokenResponse checkJWTToken(IsValidTokenRequest request) throws JOSEException, ParseException;
//
//    // AuthenticationResponse refreshToken(HttpServletRequest httpRequest) throws Exception;
//
//    // AuthenticationResponse loginAuthenticate(LoginRequest request, HttpServletRequest httpRequest);
//
//    void logout(IsValidTokenRequest request, HttpServletRequest httpRequest) throws JOSEException, ParseException;
//
//    // void passwordChangeVerification(PasswordChangeVerificationRequest request);
//
//    // void changePasswordWithCode(PasswordChangeWithCodeRequest request);
//}
