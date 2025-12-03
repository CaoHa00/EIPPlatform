package com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringincidents;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoWWMonitoringIncidentsCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String incidentName;

    @NotBlank(message = "IS_REQUIRED")
    String incidentTime;

    @NotBlank(message = "IS_REQUIRED")
    String incidentRemedy;
}