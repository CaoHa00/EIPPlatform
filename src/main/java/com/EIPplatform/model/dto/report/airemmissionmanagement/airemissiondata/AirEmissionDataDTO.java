package com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata;

import com.EIPplatform.model.dto.report.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirEmissionDataDTO {

    Long airEmissionDataId;

    String airTreatmentDesc;

    BigDecimal airEmissionCy;

    BigDecimal airEmissionPy;

    String airMonitorPeriod;

    String airMonitorFreq;

    String airMonitorLocations;

    Integer airSampleCount;

    String airQcvnStandard;

    String airAgencyName;

    String airAgencyVimcerts;

    List<AirMonitoringExceedanceDTO> airMonitoringExceedances;

    String airAutoStationLocation;

    String airAutoStationGps;

    String airAutoStationMap;

    String airAutoSourceDesc;

    String airAutoDataFrequency;

    String airAutoParamList;

    String airAutoCalibrationInfo;

    String airAutoIncidentSummary;

    String airAutoDowntimeDesc;

    List<AirAutoMonitoringStatDTO> airAutoMonitoringStats;

    List<AirAutoMonitoringIncidentDTO> airAutoMonitoringIncidents;

    String airAutoAvgCalcDesc;

    String airAutoAvgCompareDesc;

    String airAutoExceedDaysSummary;

    String airAutoStationMapFilePath;

    String airAutoAbnormalReason;

    List<AirAutoQcvnExceedanceDTO> airAutoQcvnExceedances;

    String airAutoCompletenessReview;

    String airAutoExceedConclusion;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}