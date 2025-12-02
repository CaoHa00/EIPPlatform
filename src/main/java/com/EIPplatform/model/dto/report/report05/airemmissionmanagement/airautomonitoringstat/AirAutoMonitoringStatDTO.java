package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringStatDTO {

    Long id;

    String paramName;

    Integer valDesign;

    Integer valReceived;

    Integer valError;

    Double ratioReceivedDesign;

    Double ratioErrorReceived;

}