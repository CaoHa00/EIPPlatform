package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LegalRepresentativeError implements ErrorCodeInterface {

    LEGAL_REPRESENTATIVE_NOT_FOUND(1001, "Legal representative not found", HttpStatus.NOT_FOUND),
    TAX_CODE_ALREADY_EXISTS(1002, "Tax code already exists", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;

    LegalRepresentativeError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

}
