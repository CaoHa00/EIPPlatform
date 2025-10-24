package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AuthenticationError implements ErrorCodeInterface {
    // 1500 -> 1599
    LOGIN_FAILED(1500, "Wrong email or password", HttpStatus.BAD_REQUEST),
    USER_DELETED(1501, "User has been disable", HttpStatus.BAD_REQUEST),
    USER_BLOCKED(1502, "User has been blocked", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1503, "User not found", HttpStatus.BAD_REQUEST),
    FAILED_TO_CHANGE_PASSWORD(1504, "Failed to change password", HttpStatus.BAD_REQUEST),
    SAME_OLD_PASSWORD(1505, "New password is the same with old password", HttpStatus.BAD_REQUEST),
    TOO_MANY_VERIFICATION_REQUESTS(1506, "Too many verification requests", HttpStatus.BAD_REQUEST),
    PASSWORD_CONFIRMATION_MISMATCH(1507, "Passwords mismatch", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_CODE(1508, "Invalid verification code", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED(1509, "Verification code expired", HttpStatus.BAD_REQUEST),
    USER_NOT_AUTHENTICATED(1510, "User is not authenticated", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    AuthenticationError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}