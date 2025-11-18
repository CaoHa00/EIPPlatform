package com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CarbonSinkProjectUpdateDTO {
    String projectName;
    String captureTechnology;
    BigDecimal capturedCo2AmountTons;
    String storageSiteLocation;
    String monitoringDetails;
    BigDecimal netCo2SequesteredTons;
}