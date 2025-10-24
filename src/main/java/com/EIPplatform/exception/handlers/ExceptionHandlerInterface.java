package com.EIPplatform.exception.handlers;
import org.springframework.http.ResponseEntity;

import com.EIPplatform.model.dto.api.ApiResponse;
public interface ExceptionHandlerInterface<T extends Exception> {
    boolean canHandle(Exception exception);

    ResponseEntity<ApiResponse<?>> handle(T exception);
}