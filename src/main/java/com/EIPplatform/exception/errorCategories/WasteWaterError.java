package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum WasteWaterError implements ErrorCodeInterface {
    // 1400 -> 1499
    NOT_FOUND(1400, "WasteWaterData not found", HttpStatus.NOT_FOUND),
    REPORT_NOT_FOUND(1401, "Report not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTRY(1402, "WasteWaterData already exists for report", HttpStatus.CONFLICT),
    INVALID_FLOW_RATE(1403, "Flow rate must be non-negative", HttpStatus.BAD_REQUEST),
    INVALID_MONITORING_PARAM(1404, "Invalid monitoring parameter", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(1405, "Missing required field", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    WasteWaterError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}