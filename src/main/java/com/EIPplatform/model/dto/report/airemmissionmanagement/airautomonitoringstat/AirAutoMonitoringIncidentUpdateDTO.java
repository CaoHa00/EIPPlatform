package com.EIPplatform.model.dto.report.airemmissionmanagement.airautomonitoringstat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringIncidentUpdateDTO {

    String incidentName;

    LocalDateTime incidentTime;

    String incidentRemedy;
}