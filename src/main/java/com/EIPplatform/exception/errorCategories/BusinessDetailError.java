package com.EIPplatform.exception.errorCategories;

import com.EIPplatform.exception.ErrorCodeInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum BusinessDetailError implements ErrorCodeInterface {
    TAX_CODE_DUPLICATE(1001, "Tax code {0} already exists", HttpStatus.BAD_REQUEST),
    BUSINESS_REGISTRATION_NUMBER_DUPLICATE(1002, "Business registration number {0} already exists", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_DUPLICATE(1003, "Phone number {0} already exists", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1004, "Business not found for user account ID {0}", HttpStatus.NOT_FOUND),
    BUSINESS_DETAIL_ID_NOT_FOUND(1005, "Business detail with ID {0} not found", HttpStatus.NOT_FOUND),
    ISO_CERT_FILE_NOT_FOUND(1006, "ISO certificate file for business detail ID {0} not found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    BusinessDetailError(int code, String message, HttpStatusCode statusCode) {
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

    // Helper để format message với value (e.g., taxCode)
    public String getFormattedMessage(Object... args) {
        return String.format(message, args);
    }
}