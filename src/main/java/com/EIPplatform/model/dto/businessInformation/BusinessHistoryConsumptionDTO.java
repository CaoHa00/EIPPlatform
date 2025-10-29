package com.EIPplatform.model.dto.businessInformation;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessHistoryConsumptionDTO {
    Long businessHistoryConsumptionId;
    String productVolume;
    String productUnit;
    String fuelConsumption;
    String fuelUnit;
    String electricityConsumption;
    String waterConsumption;
    
    // @NotBlank(message = "FIELD_REQUIRED")
    // String yearUpdated;
    
    @NotBlank(message = "FIELD_REQUIRED")
    UUID businessDetailId;
}
