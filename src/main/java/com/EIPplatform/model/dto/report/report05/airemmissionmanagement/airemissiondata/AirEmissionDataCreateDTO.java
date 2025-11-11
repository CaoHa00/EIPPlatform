package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirEmissionDataCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    String airTreatmentDesc;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal airEmissionCy;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal airEmissionPy;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String airMonitorPeriod;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String airMonitorFreq;

    @NotBlank(message = "IS_REQUIRED")
    String airMonitorLocations;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer airSampleCount;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String airQcvnStandard;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 255)
    String airAgencyName;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 50)
    String airAgencyVimcerts;

    @Valid
    @NotNull(message = "IS_REQUIRED")
    List<@Valid AirMonitoringExceedanceCreateDTO> airMonitoringExceedances;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 500)
    String airAutoStationLocation;

    @Size(max = 30)
    String airAutoStationGps;

    @Size(max = 255)
    String airAutoStationMap;

    @NotBlank(message = "IS_REQUIRED")
    String airAutoSourceDesc;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String airAutoDataFrequency;

    @NotBlank(message = "IS_REQUIRED")
    String airAutoParamList;

    @NotBlank(message = "IS_REQUIRED")
    String airAutoCalibrationInfo;

    String airAutoIncidentSummary;

    String airAutoDowntimeDesc;

    @Valid
    @NotNull(message = "IS_REQUIRED")
    List<@Valid AirAutoMonitoringStatCreateDTO> airAutoMonitoringStats;

    @Valid
    List<@Valid AirAutoMonitoringIncidentCreateDTO> airAutoMonitoringIncidents;

    @NotBlank(message = "IS_REQUIRED")
    String airAutoAvgCalcDesc;

    @NotBlank(message = "IS_REQUIRED")
    String airAutoAvgCompareDesc;

    String airAutoExceedDaysSummary;

    String airAutoAbnormalReason;

    @Valid
    List<@Valid AirAutoQcvnExceedanceCreateDTO> airAutoQcvnExceedances;

    @NotBlank(message = "IS_REQUIRED")
    String airAutoCompletenessReview;

    @NotBlank(message = "IS_REQUIRED")
    String airAutoExceedConclusion;
}