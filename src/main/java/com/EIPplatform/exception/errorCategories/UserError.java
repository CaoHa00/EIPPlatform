package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum UserError implements ErrorCodeInterface {
    // 1100 -> 1199
    NOT_FOUND(1100, "User account with ID {0} not found", HttpStatus.NOT_FOUND),
    ID_GENERATION_FAILED(1101, "Failed to generate ID for entity '{0}'", HttpStatus.INTERNAL_SERVER_ERROR),
    SAVE_FAILED(1102, "Failed to save entity '{0}' to database", HttpStatus.INTERNAL_SERVER_ERROR),
    MISSING_REQUIRED_FIELD(1103, "Missing required field '{0}'", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1104, "File '{0}' not found", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTITY(1105, "Entity '{0}' with value '{1}' already exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS(1106, "Unauthorized access to resource '{0}' for user '{1}'", HttpStatus.FORBIDDEN),
    FILE_UPLOAD_FAILED(1107, "Failed to upload file '{0}': {1}", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    UserError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    // Helper để format message với value (e.g., field name)
    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}