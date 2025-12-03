package com.EIPplatform.exception.errorCategories;

import com.EIPplatform.exception.ErrorCodeInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FormError implements ErrorCodeInterface {
    SURVEY_FORM_NOT_FOUND(40401, "Survey form not found", HttpStatus.NOT_FOUND),
    DIMENSION_NOT_FOUND(40402, "Dimension not found", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(40403, "Question not found", HttpStatus.NOT_FOUND),
    GROUP_DIMENSION_NOT_FOUND(40404, "Group dimension not found", HttpStatus.NOT_FOUND),
    QUESTION_OPTION_NOT_FOUND(40405, "Question option not found", HttpStatus.NOT_FOUND),
    SUBMISSION_NOT_FOUND(40406, "Submission not found", HttpStatus.NOT_FOUND),
    SUBMISSIONS_EXIST(40407, "Cannot delete Survey/Question with existing submissions", HttpStatus.BAD_REQUEST),
    DIMENSION_IN_USE(40408, "Cannot delete Dimension, there are SurveyForm/Question using this", HttpStatus.BAD_REQUEST),
    GROUP_DIMENSION_IN_USE(40409, "Cannot delete GroupDimension, there are Questions using this", HttpStatus.BAD_REQUEST),
    ANSWER_NOT_FOUND(40410, "Answer not found", HttpStatus.NOT_FOUND );

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return this.statusCode;
    }
}
