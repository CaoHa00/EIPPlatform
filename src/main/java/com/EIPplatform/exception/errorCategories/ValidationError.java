package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.EIPplatform.exception.ErrorCodeInterface;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ValidationError implements ErrorCodeInterface {
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
    LEVEL_INVALID_HIERARCHY(9012, "Invalid level for hierarchy", HttpStatus.BAD_REQUEST),
    NOT_FOR_WAREHOUSE(9013, "Warehouse is not be used in this function", HttpStatus.BAD_REQUEST),
    ONLY_FOR_FLOOR(9014, "This function is only for Floor", HttpStatus.BAD_REQUEST),
    EMPTY_REQUEST_LIST(9015, "Request list is empty", HttpStatus.BAD_REQUEST),
    BATCH_VALIDATION_FAILED(9016, "Batch validation failed", HttpStatus.BAD_REQUEST),
    EMPTY_FILE(9017, "Uploaded file is empty", HttpStatus.BAD_REQUEST),
    EMPTY_DATA_FILE(9018, "No valid data found in file", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(9019, "Invalid date range provided", HttpStatus.BAD_REQUEST),
    EXPORT_FAILED(9020, "Failed to export tree data", HttpStatus.INTERNAL_SERVER_ERROR),
    COORDINATES_OUT_OF_RANGE(9021, "Coordinates are out of valid range", HttpStatus.BAD_REQUEST),
    INVALID_PLANTED_DATE(9022, "Invalid planted date for tree", HttpStatus.BAD_REQUEST),
    COORDINATES_REQUIRED(9023, "Coordinates are required", HttpStatus.BAD_REQUEST),
    INVALID_GIRTH(9024, "Girth must be positive", HttpStatus.BAD_REQUEST),
    INVALID_HEALTH_STATUS(9025, "Invalid health status", HttpStatus.BAD_REQUEST),
    DUPLICATE_COORDINATES_IN_BATCH(9026, "Duplicate coordinates found in batch", HttpStatus.BAD_REQUEST),
    INVALID_IMAGE_URL(9027, "Invalid image URL format", HttpStatus.BAD_REQUEST),
    INVALID_MEASURED_DATE(9028, "Invalid measurement date", HttpStatus.BAD_REQUEST),
    INVALID_NAME_FORMAT(9029, "Invalid name format", HttpStatus.BAD_REQUEST),
    INVALID_SPECIES_DATA(9030, "Invalid or missing species data", HttpStatus.BAD_REQUEST),
    SOME_ACTIONS_NOT_FOUND(9031, "Some action IDs not found", HttpStatus.BAD_REQUEST),
    KEY_INVALID(9032, "Invalid message key", HttpStatus.BAD_REQUEST),
    FIELD_MIN_LENGTH(9033, "This field must be at least {0} characters", HttpStatus.BAD_REQUEST),
    FIELD_MAX_LENGTH(9034, "This field must not exceed {0} characters", HttpStatus.BAD_REQUEST),
    FIELD_RANGE(9035, "This field must be between {0} and {1}", HttpStatus.BAD_REQUEST),
    FIELD_MIN(9036, "This field must be at least {0}", HttpStatus.BAD_REQUEST),
    FIELD_MAX(9037, "This field must be at most {0}", HttpStatus.BAD_REQUEST),
    INVALID_TYPE(9038, "Invalid file type", HttpStatus.BAD_REQUEST),
    TOO_LARGE(9039, "File is too large", HttpStatus.BAD_REQUEST),
    DUPLICATE_VALUE(9040, "Duplicate value found", HttpStatus.BAD_REQUEST),

    // Permit-specific validation errors (9041-9060)
    INVALID_STATE(9041, "Invalid state for this operation", HttpStatus.BAD_REQUEST),
    MAIN_PERMIT_ALREADY_EXISTS(9042, "Main environmental permit already exists for this business", HttpStatus.BAD_REQUEST),
    COMPONENT_PERMITS_EXIST(9043, "Cannot create main permit when component permits exist", HttpStatus.BAD_REQUEST),
    MAIN_PERMIT_EXISTS(9044, "Cannot create component permits when main permit exists", HttpStatus.BAD_REQUEST),
    INVALID_PERMIT_NUMBER(9045, "Invalid permit number format", HttpStatus.BAD_REQUEST),
    DUPLICATE_PERMIT_NUMBER(9046, "Permit number already exists", HttpStatus.BAD_REQUEST),
    INVALID_ISSUE_DATE(9047, "Issue date cannot be in the future", HttpStatus.BAD_REQUEST),
    INVALID_PERMIT_TYPE(9048, "Invalid permit type", HttpStatus.BAD_REQUEST),
    PERMIT_FILE_REQUIRED(9049, "Permit file is required", HttpStatus.BAD_REQUEST),
    INVALID_FILE_EXTENSION(9050, "Invalid file extension. Only PDF, JPG, JPEG, PNG are allowed", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED(9051, "File size exceeds maximum allowed size (10MB)", HttpStatus.BAD_REQUEST),
    PERMIT_ALREADY_ACTIVE(9052, "Permit is already active", HttpStatus.BAD_REQUEST),
    PERMIT_ALREADY_INACTIVE(9053, "Permit is already inactive", HttpStatus.BAD_REQUEST),
    BATCH_SIZE_EXCEEDED(9054, "Batch size exceeds maximum allowed limit", HttpStatus.BAD_REQUEST),
    DUPLICATE_IN_BATCH(9055, "Duplicate permit numbers found in batch request", HttpStatus.BAD_REQUEST),
    INVALID_ISSUER_ORG(9056, "Invalid issuer organization", HttpStatus.BAD_REQUEST),
    INVALID_PROJECT_NAME(9057, "Invalid project name", HttpStatus.BAD_REQUEST),
    PERMIT_EXPIRED(9058, "Permit has expired", HttpStatus.BAD_REQUEST),
    NO_PERMITS_TO_DELETE(9059, "No permits found to delete", HttpStatus.BAD_REQUEST),
    CANNOT_DELETE_ACTIVE_PERMIT(9060, "Cannot delete active permit. Please deactivate first", HttpStatus.BAD_REQUEST),

    // Form-specific validation errors
    INVALID_QUESTION_TYPE(9061, "Question type is not valid", HttpStatus.BAD_REQUEST),
    DISPLAY_ORDER_ALREADY_EXISTS(9062, "displayOrder already used by another item.", HttpStatus.BAD_REQUEST),
    FORM_INACTIVE(9063, "Form is inactive.", HttpStatus.BAD_REQUEST),
    FORM_EXPIRED(9064, "Form has expired.", HttpStatus.BAD_REQUEST),
    REQUIRED_QUESTION(9065, "This question is required.", HttpStatus.BAD_REQUEST),
    ANSWER_ALREADY_EXISTS(9066, "Answer already exists.", HttpStatus.BAD_REQUEST),
    ANSWER_EMPTY_VALUE(9067, "Answer value cannot be empty.", HttpStatus.BAD_REQUEST),
    SUBMISSION_DELETED(9068, "This submission has been deleted.", HttpStatus.BAD_REQUEST);


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
