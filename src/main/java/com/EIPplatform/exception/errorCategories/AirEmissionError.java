package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AirEmissionError implements ErrorCodeInterface {
    // 1300 -> 1399
    NOT_FOUND(1300, "AirEmissionData not found", HttpStatus.NOT_FOUND),
    REPORT_NOT_FOUND(1301, "Report not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTRY(1302, "AirEmissionData already exists for report", HttpStatus.CONFLICT),
    INVALID_VOLUME(1303, "Volume must be non-negative", HttpStatus.BAD_REQUEST),
    INVALID_MONITORING_PARAM(1304, "Invalid monitoring parameter", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(1305, "Missing required field", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    AirEmissionError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}