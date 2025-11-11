package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ProductError implements ErrorCodeInterface {
    PRODUCT_NOT_FOUND(4001, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_NAME_ALREADY_EXISTS(4002, "Product name already exists", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;

    ProductError(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}