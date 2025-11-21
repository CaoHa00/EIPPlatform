package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringIncidentCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 255)
    String incidentName;

    @NotBlank(message = "IS_REQUIRED")
    String incidentTime;

    @NotBlank(message = "IS_REQUIRED")
    String incidentRemedy;
}