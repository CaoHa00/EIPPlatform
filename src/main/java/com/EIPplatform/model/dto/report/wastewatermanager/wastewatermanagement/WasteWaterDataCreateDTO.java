package com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement;

import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsCreateDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsCreateDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesCreateDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteWaterDataCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String treatmentWwDesc;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double domWwCy;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double domWwPy;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double domWwDesign;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double industrialWwCy;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double industrialWwPy;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double industrialWwDesign;

    @DecimalMin(value = "0.0", inclusive = true)
    Double coolingWaterCy;

    @DecimalMin(value = "0.0", inclusive = true)
    Double coolingWaterPy;

    @DecimalMin(value = "0.0", inclusive = true)
    Double coolingWaterDesign;

    @NotBlank(message = "IS_REQUIRED")
    String connectionStatusDesc;

    @NotBlank(message = "IS_REQUIRED")
    String domMonitorPeriod;

    @NotBlank(message = "IS_REQUIRED")
    String domMonitorFreq;

    @NotBlank(message = "IS_REQUIRED")
    String domMonitorLocations;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer domSampleCount;

    @NotBlank(message = "IS_REQUIRED")
    String domQcvnStandard;

    @NotBlank(message = "IS_REQUIRED")
    String domAgencyName;

    @NotBlank(message = "IS_REQUIRED")
    String domAgencyVimcerts;

    @NotBlank(message = "IS_REQUIRED")
    String indMonitorPeriod;

    @NotBlank(message = "IS_REQUIRED")
    String indMonitorFreq;

    @NotBlank(message = "IS_REQUIRED")
    String indMonitorLocations;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer indSampleCount;

    @NotBlank(message = "IS_REQUIRED")
    String indQcvnStandard;

    @NotBlank(message = "IS_REQUIRED")
    String indAgencyName;

    @NotBlank(message = "IS_REQUIRED")
    String indAgencyVimcerts;

    @NotNull(message = "IS_REQUIRED")
    List<WasteWaterMonitoringExceedancesCreateDTO> monitoringExceedances;

    @NotBlank(message = "IS_REQUIRED")
    String autoStationLocation;

    String autoStationGps;

    String autoSourceDesc;

    @NotBlank(message = "IS_REQUIRED")
    String autoDataFrequency;

    @NotBlank(message = "IS_REQUIRED")
    String autoCalibrationInfo;

    String autoIncidentSummary;

    String autoDowntimeDesc;

    @NotNull(message = "IS_REQUIRED")
    List<AutoWWMonitoringStatsCreateDTO> monitoringStats;

    List<AutoWWMonitoringIncidentsCreateDTO> monitoringIncidents;

    String autoExceedDaysSummary;

    String autoAbnormalReason;

    List<AutoWWQcvnExceedancesCreateDTO> qcvnExceedances;

    @NotBlank(message = "IS_REQUIRED")
    String autoCompletenessReview;

    String autoExceedSummary;

}