package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ReportError implements ErrorCodeInterface {
    // 1000 -> 1999 for Report-related errors
    BUSINESS_NOT_FOUND(1000, "Business not found", HttpStatus.NOT_FOUND),
    REPORT_NOT_FOUND(1001, "Report not found", HttpStatus.NOT_FOUND),
    DRAFT_NOT_FOUND(1002, "Draft data not found in cache", HttpStatus.NOT_FOUND),
    DRAFT_INCOMPLETE(1003, "Report draft is not complete yet. All sections must be filled before saving to database. Current completion: {0}%", HttpStatus.BAD_REQUEST),

    OPERATION_FAILED(1010, "Report operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    SAVE_TO_DATABASE_FAILED(1011, "Failed to save report data to database", HttpStatus.INTERNAL_SERVER_ERROR),
    CACHE_OPERATION_FAILED(1012, "Failed to operate on report cache", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR(1013, "Database error occurred while processing report", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_OPERATION(1020, "Invalid operation for this report", HttpStatus.BAD_REQUEST),
    CANNOT_SAVE_INCOMPLETE_DRAFT(1021, "Cannot save incomplete draft to database", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED(1022, "You don't have permission to access this report", HttpStatus.FORBIDDEN);

    int code;
    String message;
    HttpStatusCode statusCode;

    ReportError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getFormattedMessage(Object... params) {
        return java.text.MessageFormat.format(this.message, params);
    }
}