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
public class AirAutoMonitoringIncidentDTO {

    Long id;

    String incidentName;

    String incidentTime;

    String incidentRemedy;

}