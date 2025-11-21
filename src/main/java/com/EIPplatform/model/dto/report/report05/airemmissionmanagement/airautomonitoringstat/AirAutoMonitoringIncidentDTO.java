package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringIncidentDTO {

    Long id;

    String incidentName;

    String incidentTime;

    String incidentRemedy;

}