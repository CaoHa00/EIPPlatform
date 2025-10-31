package com.EIPplatform.model.dto.report.report.wastewatermanagerDTO;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AutoWWMonitoringIncidentsDTO implements Serializable {
    Long incidentId;
    String incidentName;
    LocalDateTime incidentTime;
    String incidentRemedy;
}