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
    NOT_FOUND(1100, "User account not found", HttpStatus.NOT_FOUND),
    ID_GENERATION_FAILED(1101, "Unable to generate ID for the entity", HttpStatus.INTERNAL_SERVER_ERROR),
    SAVE_FAILED(1102, "Unable to save entity to the database", HttpStatus.INTERNAL_SERVER_ERROR),
    MISSING_REQUIRED_FIELD(1103, "A required field is missing", HttpStatus.BAD_REQUEST),
    FILE_NOT_FOUND(1104, "Requested file could not be found", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTITY(1105, "An entity with this value already exists", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS(1106, "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    FILE_UPLOAD_FAILED(1107, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);


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