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
    NOT_FOUND(1100, "User not found", HttpStatus.NOT_FOUND),
    ID_GENERATION_FAILED(1101, "Failed to generate entity ID", HttpStatus.INTERNAL_SERVER_ERROR),
    // hoặc
    SAVE_FAILED(1102, "Failed to save entity to database", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    UserError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}