package com.EIPplatform.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.EIPplatform.exception.ErrorCode;
import com.EIPplatform.exception.errorCategories.SystemError;
import com.EIPplatform.exception.exceptions.AppException;
import com.EIPplatform.model.dto.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String INVALID_TOKEN_PREFIX = "Invalid token: ";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, ErrorCode> errorCodeMap;

    public JwtAuthenticationEntryPoint() {
        this.errorCodeMap = initializeErrorCodeMap();
    }

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        ErrorCode errorCode = determineErrorCode(authException);
        writeErrorResponse(response, errorCode);
    }

    private ErrorCode determineErrorCode(AuthenticationException authException) {
        AppException appException = findAppException(authException);
        if (appException != null) {
            return appException.getErrorCode();
        }

        String errorMessage = authException.getMessage();
        if (errorMessage != null && errorCodeMap.containsKey(errorMessage)) {
            return errorCodeMap.get(errorMessage);
        }

        return createErrorCode(SystemError.UNAUTHENTICATED);
    }

    private AppException findAppException(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof AppException) {
                return (AppException) current;
            }
            current = current.getCause();
        }
        return null;
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private Map<String, ErrorCode> initializeErrorCodeMap() {
        Map<String, ErrorCode> map = new HashMap<>();

        map.put(
                buildErrorMessage(SystemError.FAILED_TO_REFRESH),
                createErrorCode(SystemError.FAILED_TO_REFRESH));

        map.put(
                buildErrorMessage(SystemError.SESSION_INVALID),
                createErrorCode(SystemError.SESSION_INVALID));

        return map;
    }

    private String buildErrorMessage(SystemError systemError) {
        return INVALID_TOKEN_PREFIX + systemError.getMessage();
    }

    private ErrorCode createErrorCode(SystemError systemError) {
        return new ErrorCode(
                systemError.getCode(),
                systemError.getMessage(),
                systemError.getStatusCode());
    }
}