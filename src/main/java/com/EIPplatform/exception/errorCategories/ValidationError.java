package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ValidationError implements ErrorCodeInterface{
        // 9000 -> 9999
    INVALID_FIELDS(9000, "Invalid fields", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED(9001, "This field is required", HttpStatus.BAD_REQUEST),
    FIELD_POSITIVE(9002, "This field must be positive", HttpStatus.BAD_REQUEST),
    EMAIL_FORMAT(9003, "Wrong email format", HttpStatus.BAD_REQUEST),
    FIELD_POSITIVE_AND_ZERO(9004, "This field must be equal or greater than zero", HttpStatus.BAD_REQUEST),
    DATE_INVALID(9005, "Invalid date", HttpStatus.BAD_REQUEST),
    FAILED_TO_GET_NEW_DEVICE(9006, "Failed to get new device list", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_DBH(9007, "DBH must be positive", HttpStatus.BAD_REQUEST),
    INVALID_HEIGHT(9008, "Height must be positive", HttpStatus.BAD_REQUEST),
    INVALID_CANOPY_DIAMETER(9009, "Canopy diameter must be positive", HttpStatus.BAD_REQUEST),
    EXCEED_AVAILABLE_UNITS(9010, "Transport quantity exceeds available units", HttpStatus.BAD_REQUEST),
    NEGATIVE_VALUE_AFTER_UPDATING(9011, "Negative value after updating", HttpStatus.BAD_REQUEST),
    LEVEL_INVALID_HIERARCHY(9021, "Invalid level for hierarchy", HttpStatus.BAD_REQUEST),
    NOT_FOR_WAREHOUSE(9022, "Warehouse is not be used in this function", HttpStatus.BAD_REQUEST),
    ONLY_FOR_FLOOR(9023, "This function is only for Floor", HttpStatus.BAD_REQUEST),
    EMPTY_REQUEST_LIST(9024, "Request list is empty", HttpStatus.BAD_REQUEST),
    BATCH_VALIDATION_FAILED(9025, "Batch validation failed", HttpStatus.BAD_REQUEST),
    EMPTY_FILE(9026, "Uploaded file is empty", HttpStatus.BAD_REQUEST),
    EMPTY_DATA_FILE(9027, "No valid data found in file", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(9028, "Invalid date range provided", HttpStatus.BAD_REQUEST),
    EXPORT_FAILED(9029, "Failed to export tree data", HttpStatus.INTERNAL_SERVER_ERROR),
    COORDINATES_OUT_OF_RANGE(9030, "Coordinates are out of valid range", HttpStatus.BAD_REQUEST),
    INVALID_PLANTED_DATE(9031, "Invalid planted date for tree", HttpStatus.BAD_REQUEST),

    COORDINATES_REQUIRED(9032, "Coordinates are required", HttpStatus.BAD_REQUEST),
    INVALID_GIRTH(9033, "Girth must be positive", HttpStatus.BAD_REQUEST),
    INVALID_HEALTH_STATUS(9034, "Invalid health status", HttpStatus.BAD_REQUEST),
    DUPLICATE_COORDINATES_IN_BATCH(9035, "Duplicate coordinates found in batch", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_URL(9036, "Invalid image URL format", HttpStatus.BAD_REQUEST),
    INVALID_MEASURED_DATE(9037, "Invalid measurement date", HttpStatus.BAD_REQUEST),
    INVALID_NAME_FORMAT(9038, "Invalid name format", HttpStatus.BAD_REQUEST),
    INVALID_SPECIES_DATA(9039, "Invalid or missing species data", HttpStatus.BAD_REQUEST),
    KEY_INVALID(9999, "Invalid message key", HttpStatus.BAD_REQUEST),
    SOME_ACTIONS_NOT_FOUND(9040, "Some action IDs not found", HttpStatus.BAD_REQUEST),
    FIELD_MIN_LENGTH(9032, "This field must be at least {0} characters", HttpStatus.BAD_REQUEST),
    FIELD_MAX_LENGTH(9033, "This field must be at least {0} characters", HttpStatus.BAD_REQUEST),
    FIELD_RANGE(9034, "This field must be between {0} and {1}", HttpStatus.BAD_REQUEST),
    FIELD_MIN(9035, "This field must be at least {0}", HttpStatus.BAD_REQUEST),
    FIELD_MAX(9036, "This field must be at most {0}", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;

    ValidationError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getFormattedMessage(Object... params) {
        return java.text.MessageFormat.format(this.message, params);
    }

}
