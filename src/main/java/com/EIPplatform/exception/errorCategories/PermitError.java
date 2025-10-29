package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum PermitError implements ErrorCodeInterface {
    // 8000 -> 8999
    NOT_FOUND(8000, "Permit not found", HttpStatus.NOT_FOUND),
    MAIN_PERMIT_NOT_FOUND(8001, "Main environmental permit not found", HttpStatus.NOT_FOUND),
    COMPONENT_PERMIT_NOT_FOUND(8002, "Component permit not found", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND(8003, "Permit file not found", HttpStatus.NOT_FOUND),
    NO_PERMITS_FOUND(8004, "No permits found for this business", HttpStatus.NOT_FOUND),

    OPERATION_FAILED(8010, "Permit operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_FAILED(8011, "Failed to upload permit file", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DOWNLOAD_FAILED(8012, "Failed to download permit file", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_FAILED(8013, "Failed to delete permit file", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR(8014, "Database error occurred while processing permit", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_OPERATION(8020, "Invalid operation for this permit", HttpStatus.BAD_REQUEST),
    CANNOT_MODIFY_EXPIRED(8021, "Cannot modify expired permit", HttpStatus.BAD_REQUEST),
    CANNOT_ACTIVATE_EXPIRED(8022, "Cannot activate expired permit", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED(8023, "You don't have permission to access this permit", HttpStatus.FORBIDDEN);

    int code;
    String message;
    HttpStatusCode statusCode;

    PermitError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getFormattedMessage(Object... params) {
        return java.text.MessageFormat.format(this.message, params);
    }
}