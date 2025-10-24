package com.EIPplatform.exception;
import org.springframework.http.HttpStatusCode;
public interface ErrorCodeInterface {
    int getCode();

    String getMessage();

    HttpStatusCode getStatusCode();
}