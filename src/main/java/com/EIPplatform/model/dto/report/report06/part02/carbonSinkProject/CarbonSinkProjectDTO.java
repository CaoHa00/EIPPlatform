package com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarbonSinkProjectDTO {
    UUID carbonSinkProjectId;
    String projectName;
    String captureTechnology;
    BigDecimal capturedCo2AmountTons;
    String storageSiteLocation;
    String monitoringDetails;
    BigDecimal netCo2SequesteredTons;
}