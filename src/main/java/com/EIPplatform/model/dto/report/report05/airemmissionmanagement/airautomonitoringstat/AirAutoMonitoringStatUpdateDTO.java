package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringStatUpdateDTO {

    String paramName;

    Integer valDesign;

    Integer valReceived;

    Integer valError;

    Double ratioReceivedDesign;

    Double ratioErrorReceived;
}