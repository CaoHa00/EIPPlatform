package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirMonitoringExceedanceDTO {

    Long id;

    String pointName;

    String pointSymbol;

    LocalDate monitoringDate;

    String longitude;

    String latitude;

    String exceededParam;

    Double resultValue;

    Double qcvnLimit;

}