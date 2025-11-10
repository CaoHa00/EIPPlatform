package com.EIPplatform.model.dto.businessInformation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BusinessHistoryConsumptionUpdateDTO {

    Integer productVolumeCy;

    Integer productVolumePy;

    String productUnitCy;

    String productUnitPy;

    Integer fuelConsumptionCy;

    Integer fuelConsumptionPy;

    String fuelUnitCy;

    String fuelUnitPy;

    Integer electricityConsumptionCy;

    Integer electricityConsumptionPy;

    Integer waterConsumptionCy;

    Integer waterConsumptionPy;

    String yearUpdated;

    UUID businessDetailId;
}