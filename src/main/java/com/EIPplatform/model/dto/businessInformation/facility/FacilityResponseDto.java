package com.EIPplatform.model.dto.businessInformation.facility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityResponseDto {
    UUID facilityId;
    UUID businessId;
    String areaName;
    String areaType;
    String areaFunction;
    String floorCount;
    String buildArea;
    String floorArea;
    LocalDateTime createdAt;
}
