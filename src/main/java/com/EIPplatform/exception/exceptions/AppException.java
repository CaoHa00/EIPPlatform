package com.EIPplatform.exception.exceptions;

import java.util.Collections;
import java.util.Map;

import com.EIPplatform.exception.ErrorCode;
import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AppException extends RuntimeException  {
    ErrorCode errorCode;
    Map<String, Object> context;

    public AppException(ErrorCodeInterface errorCodeInterface) {
        this(new ErrorCode(errorCodeInterface.getCode(), errorCodeInterface.getMessage(), 
                          errorCodeInterface.getStatusCode()));
    }

    public AppException(ErrorCode errorCode) {
        this(errorCode, Collections.emptyMap());
    }

    public AppException(ErrorCode errorCode, Map<String, Object> context) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.context = context;
    }

    public AppException(ErrorCodeInterface errorCodeInterface, Map<String, Object> context) {
        this(new ErrorCode(errorCodeInterface.getCode(), errorCodeInterface.getMessage(), 
                          errorCodeInterface.getStatusCode()), context);
    }
}
