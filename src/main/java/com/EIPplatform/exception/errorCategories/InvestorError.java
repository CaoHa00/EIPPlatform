package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum InvestorError implements ErrorCodeInterface {
    INVESTOR_NOT_FOUND(2001, "Investor not found", HttpStatus.NOT_FOUND),
    TAX_CODE_ALREADY_EXISTS(2002, "Tax code already exists", HttpStatus.BAD_REQUEST),
    INVALID_INVESTOR_TYPE(2003, "Invalid investor type", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;

    InvestorError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}