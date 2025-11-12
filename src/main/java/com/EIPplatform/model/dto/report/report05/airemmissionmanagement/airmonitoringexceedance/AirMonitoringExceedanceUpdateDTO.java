package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirMonitoringExceedanceUpdateDTO {

    String pointName;

    String pointSymbol;

    java.time.LocalDate monitoringDate;

    String longitude;

    String latitude;

    String exceededParam;

    BigDecimal resultValue;

    BigDecimal qcvnLimit;
}