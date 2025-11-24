package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirMonitoringExceedanceDTO {

    Long id;

    String pointName;

    String pointSymbol;

    String monitoringDate;

    String longitude;

    String latitude;

    String exceededParam;

    Double resultValue;

    Double qcvnLimit;

}