package com.EIPplatform.model.dto.businessInformation;


import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessHistoryConsumptionCreateDTO {

    @NotNull(message = "IS_REQUIRED")
    Integer productVolumeCy;

    @NotNull(message = "IS_REQUIRED")
    Integer productVolumePy;

    @NotBlank(message = "IS_REQUIRED")
    String productUnitCy;

    @NotBlank(message = "IS_REQUIRED")
    String productUnitPy;

    @NotNull(message = "IS_REQUIRED")
    Integer fuelConsumptionCy;

    @NotNull(message = "IS_REQUIRED")
    Integer fuelConsumptionPy;

    @NotBlank(message = "IS_REQUIRED")
    String fuelUnitCy;

    @NotBlank(message = "IS_REQUIRED")
    String fuelUnitPy;

    @NotNull(message = "IS_REQUIRED")
    Integer electricityConsumptionCy;

    @NotNull(message = "IS_REQUIRED")
    Integer electricityConsumptionPy;

    @NotNull(message = "IS_REQUIRED")
    Integer waterConsumptionCy;

    @NotNull(message = "IS_REQUIRED")
    Integer waterConsumptionPy;

    @NotNull(message = "IS_REQUIRED")
    UUID businessDetailId;
}