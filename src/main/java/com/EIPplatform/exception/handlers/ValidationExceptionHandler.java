package com.EIPplatform.exception.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.EIPplatform.exception.ErrorCode;
import com.EIPplatform.exception.exceptions.ValidationException;
import com.EIPplatform.model.dto.api.ApiResponse;


@Component
public class ValidationExceptionHandler implements ExceptionHandlerInterface<ValidationException> {
    
    @Override
    public boolean canHandle(Exception exception) {
        return exception instanceof ValidationException;
    }
    
    @Override
    public ResponseEntity<ApiResponse<?>> handle(ValidationException exception) {
        List<Map<String, Object>> errorDetails = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("field", fieldError.getField());
                    error.put("errorCode", fieldError.getErrorCode());
                    error.put("message", fieldError.getMessage());
                    return error;
                })
                .collect(Collectors.toList());
        
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .result(errorDetails)
                .build();
        
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}
