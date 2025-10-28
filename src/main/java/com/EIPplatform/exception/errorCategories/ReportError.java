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
    // 1200 -> 1299
    REPORT_NOT_FOUND(1200, "Report not found", HttpStatus.NOT_FOUND),
    BUSINESS_DETAIL_NOT_FOUND(1201, "Business detail not found", HttpStatus.NOT_FOUND),
    REPORT_TYPE_NOT_FOUND(1202, "Report type not found", HttpStatus.NOT_FOUND),
    REPORT_STATUS_NOT_FOUND(1203, "Report status not found", HttpStatus.NOT_FOUND),
    REPORT_DUPLICATE(1204, "Report already exists", HttpStatus.CONFLICT),
    FORBIDDEN_BUSINESS_ACCESS(1205, "Forbidden access to business", HttpStatus.FORBIDDEN),
    REPORT_STATUS_DUPLICATE(1207, "Report status already exists",HttpStatus.BAD_REQUEST),
    INVALID_REPORT_OPERATION(1206, "Invalid operation on report", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ReportError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}