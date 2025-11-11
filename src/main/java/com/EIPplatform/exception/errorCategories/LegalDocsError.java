package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum LegalDocsError implements ErrorCodeInterface {
    LEGAL_DOC_NOT_FOUND(3001, "Legal document not found", HttpStatus.NOT_FOUND),
    LEGAL_DOC_NUMBER_ALREADY_EXISTS(3002, "Legal document number already exists for this organization", HttpStatus.BAD_REQUEST),
    INVESTOR_ORGANIZATION_NOT_FOUND(3003, "Investor organization not found", HttpStatus.NOT_FOUND),
    PROJECT_NAME_REQUIRED_FOR_GPMT(3004, "Project name is required for GPMT type documents", HttpStatus.BAD_REQUEST);

     int code;
    String message;
    HttpStatusCode statusCode;

    LegalDocsError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}