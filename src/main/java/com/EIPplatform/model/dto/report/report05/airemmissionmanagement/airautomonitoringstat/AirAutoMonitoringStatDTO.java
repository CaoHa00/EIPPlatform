package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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

    BigDecimal ratioReceivedDesign;

    BigDecimal ratioErrorReceived;

}