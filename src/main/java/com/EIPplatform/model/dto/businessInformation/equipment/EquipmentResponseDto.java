package com.EIPplatform.model.dto.businessInformation.equipment;

import com.google.api.client.util.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentResponseDto {
    UUID equipmentId;
    UUID businessDetailId;
    String equipmentName;
    String equipmentSpecifications;
    Integer equipmentQuantity;
    String equipmentFuelType;
    String equipmentPurpose;
    String equipmentCondition;
    String equipmentOrigin;
    LocalDateTime createdAt;
}
