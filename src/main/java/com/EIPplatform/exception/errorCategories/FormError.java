package com.EIPplatform.exception.errorCategories;

import com.EIPplatform.exception.ErrorCodeInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FormError implements ErrorCodeInterface {
    SURVEY_FORM_NOT_FOUND(40401, "Survey form not found", HttpStatus.NOT_FOUND),
    SURVEY_FORM_CATEGORY_NOT_FOUND(40402, "Survey form category not found", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(40403, "Question not found", HttpStatus.NOT_FOUND),
    QUESTION_CATEGORY_NOT_FOUND(40404, "Question category not found", HttpStatus.NOT_FOUND),
    QUESTION_OPTION_NOT_FOUND(40405, "Question option not found", HttpStatus.NOT_FOUND),
    SUBMISSION_NOT_FOUND(40406, "Submission not found", HttpStatus.NOT_FOUND),
    SUBMISSIONS_EXIST(40407, "Cannot delete Survey/Question with existing submissions", HttpStatus.BAD_REQUEST),
    SURVEY_FORM_CATEGORY_IN_USE(40408, "Cannot delete SurveyFormCategory, there are SurveyForm/Question using this", HttpStatus.BAD_REQUEST),
    QUESTION_CATEGORY_IN_USE(40409, "Cannot delete QuestionCategory, there are Questions using this", HttpStatus.BAD_REQUEST),
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
