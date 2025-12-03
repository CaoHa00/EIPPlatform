package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata;

import java.time.LocalDateTime;
import java.util.List;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceDTO;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirEmissionDataDTO {

    Long airEmissionDataId;

    String airTreatmentDesc;

    Double airEmissionCy;

    Double airEmissionPy;

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