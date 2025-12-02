package com.EIPplatform.configuration;

import java.util.Map;

import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.EIPplatform.exception.exceptions.AppException;
import com.EIPplatform.service.authentication.JWTServiceImplementation;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

// ================== code for verify JWT from header bearer ==================

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJWTDecoder implements JwtDecoder {

    JWTServiceImplementation JWTService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {

            SignedJWT signedJWT = JWTService.verifyToken(token);

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            Map<String, Object> headers = signedJWT.getHeader().toJSONObject();
            Map<String, Object> claims = claimsSet.getClaims();

            return new Jwt(token, claimsSet.getIssueTime().toInstant(),
                    claimsSet.getExpirationTime().toInstant(),
                    headers, claims);

        } catch (AppException e) {
            throw new BadJwtException("JWT validation failed", e);
        } catch (Exception e) {
            throw new BadJwtException("Invalid token: " + e.getMessage(), e);
        }
    }
}
// ================== code for verify JWT from header bearer ==================