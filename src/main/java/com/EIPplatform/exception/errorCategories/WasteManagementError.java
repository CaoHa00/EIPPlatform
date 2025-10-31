package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum WasteManagementError implements ErrorCodeInterface {
    // 1200 -> 1299
    NOT_FOUND(1200, "WasteManagementData not found", HttpStatus.NOT_FOUND),
    REPORT_NOT_FOUND(1201, "Report not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTRY(1202, "WasteManagementData already exists for report", HttpStatus.CONFLICT),
    INVALID_VOLUME(1203, "Volume must be non-negative", HttpStatus.BAD_REQUEST),
    INVALID_TREATMENT_METHOD(1204, "Invalid treatment method", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD(1205, "Missing required field", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    WasteManagementError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}