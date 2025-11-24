package com.EIPplatform.exception.errorCategories;

import com.EIPplatform.exception.ErrorCodeInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ResourcesSavingAndReductionError implements ErrorCodeInterface {
    // 4300 -> 4399
    NOT_FOUND(4300, "ResourcesSavingAndReduction not found", HttpStatus.NOT_FOUND),
    REPORT_NOT_FOUND(4301, "Report not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTRY(4302, "ResourcesSavingAndReduction already exists for report", HttpStatus.CONFLICT),
    MISSING_REQUIRED_FIELD(4303, "Missing required field", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ResourcesSavingAndReductionError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}