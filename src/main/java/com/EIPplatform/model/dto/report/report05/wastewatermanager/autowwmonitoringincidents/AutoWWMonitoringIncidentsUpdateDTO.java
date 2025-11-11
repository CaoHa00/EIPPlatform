package com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringincidents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoWWMonitoringIncidentsUpdateDTO {
    String incidentName;
    String incidentTime;
    String incidentRemedy;
}