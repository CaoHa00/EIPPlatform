package com.EIPplatform.exception.errorCategories;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import com.EIPplatform.exception.ErrorCodeInterface;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum InventoryResultError implements ErrorCodeInterface {
    REPORT_NOT_FOUND(6001, "Report06 not found", HttpStatus.NOT_FOUND),
    INVENTORY_RESULT_ALREADY_EXISTS(6002, "Inventory result data already exists for this report", HttpStatus.BAD_REQUEST),
    INVALID_METHOD_COLLECT_DATA_SOURCES(6003, "Invalid method collect data sources", HttpStatus.BAD_REQUEST),
    INVALID_CALCULATION_APPROACH(6004, "Invalid calculation approach", HttpStatus.BAD_REQUEST),
    EMISSION_DATA_MISSING(6005, "Emission data is required for inventory result", HttpStatus.BAD_REQUEST),
    MONTHLY_DATA_INVALID(6006, "Monthly emission data must be between 1-12 months and value >= 0", HttpStatus.BAD_REQUEST),
    RESULT_ENTITY_MISSING(6007, "Result entity is required for inventory result", HttpStatus.BAD_REQUEST),
    UNCERTAINTY_EVALUATION_MISSING(6008, "Uncertainty evaluation is required for each emission source", HttpStatus.BAD_REQUEST),
    INVALID_UNCERTAINTY_LEVEL(6009, "Invalid uncertainty level (AD or EF)", HttpStatus.BAD_REQUEST),
    TOTAL_ANNUAL_DATA_CALCULATION_FAILED(6010, "Failed to calculate total annual data from monthly values", HttpStatus.INTERNAL_SERVER_ERROR),
    INTENSITY_RATIO_CALCULATION_FAILED(6011, "Failed to calculate intensity ratio result", HttpStatus.INTERNAL_SERVER_ERROR),
    COMBINED_UNCERTAINTY_CALCULATION_FAILED(6012, "Failed to calculate combined uncertainty", HttpStatus.INTERNAL_SERVER_ERROR);

    int code;
    String message;
    HttpStatusCode statusCode;

    InventoryResultError(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}