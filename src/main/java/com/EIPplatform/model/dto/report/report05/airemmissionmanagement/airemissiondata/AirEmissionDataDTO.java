package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;
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