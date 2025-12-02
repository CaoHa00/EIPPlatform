package com.EIPplatform.exception.errorCategories;

import com.EIPplatform.exception.ErrorCodeInterface;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ObjectError implements ErrorCodeInterface {
    ENTITY_NOT_FOUND(4001, "Requested entity not found", HttpStatus.NOT_FOUND),
    BAD_REQUEST(4002, "Invalid field", HttpStatus.BAD_REQUEST),
    UNAUTHORIZE(4003, "Unauthorized user", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(4004, "No permission", HttpStatus.UNAUTHORIZED),
    ERROR(4005, "Server error", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode statusCode;

    ObjectError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
