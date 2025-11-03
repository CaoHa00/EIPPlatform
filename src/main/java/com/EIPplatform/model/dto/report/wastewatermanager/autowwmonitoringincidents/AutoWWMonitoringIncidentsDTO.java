package com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringincidents;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoWWMonitoringIncidentsDTO {
    Long incidentId;
    String incidentName;
    String incidentTime;
    String incidentRemedy;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}