package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import com.EIPplatform.exception.ErrorCodeInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum OperationalActivityError implements ErrorCodeInterface {
    REPORT_NOT_FOUND(7001, "Report06 not found", HttpStatus.NOT_FOUND),
    OPERATIONAL_ACTIVITY_ALREADY_EXISTS(7002, "Operational activity data already exists for this report", HttpStatus.BAD_REQUEST),
    INVALID_SCALE_CAPACITY_ID(7003, "Invalid scale capacity ID", HttpStatus.BAD_REQUEST),
    FACILITY_NOT_FOUND(7004, "One or more facilities not found", HttpStatus.BAD_REQUEST),
    PROCESS_NOT_FOUND(7005, "One or more processes not found", HttpStatus.BAD_REQUEST),
    EQUIPMENT_NOT_FOUND(7006, "One or more equipments not found", HttpStatus.BAD_REQUEST),
    EMISSION_SOURCES_REQUIRED(7007, "Emission sources are required", HttpStatus.BAD_REQUEST),
    INVALID_SOURCE_SCOPE(7008, "Invalid source scope (must be 1-3)", HttpStatus.BAD_REQUEST),
    SOURCE_CODE_DUPLICATE(7009, "Source code must be unique within the report", HttpStatus.BAD_REQUEST),
    INVALID_CAPTURE_TECHNOLOGY(7010, "Invalid capture technology", HttpStatus.BAD_REQUEST),
    CARBON_SINK_AMOUNT_NEGATIVE(7011, "Captured CO2 amount must be >= 0", HttpStatus.BAD_REQUEST),
    LIMITATIONS_DESCRIPTION_TOO_LONG(7012, "Limitation description exceeds maximum length", HttpStatus.BAD_REQUEST),
    DATA_MANAGEMENT_PROCEDURE_REQUIRED(7013, "Data management procedure is required", HttpStatus.BAD_REQUEST),
    EMISSION_FACTOR_SOURCE_REQUIRED(7014, "Emission factor source is required", HttpStatus.BAD_REQUEST);

    int code;
    String message;
    HttpStatusCode statusCode;

    OperationalActivityError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}