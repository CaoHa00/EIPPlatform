package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringIncidentCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 255)
    String incidentName;

    @NotNull(message = "IS_REQUIRED")
    LocalDateTime incidentTime;

    @NotBlank(message = "IS_REQUIRED")
    String incidentRemedy;
}