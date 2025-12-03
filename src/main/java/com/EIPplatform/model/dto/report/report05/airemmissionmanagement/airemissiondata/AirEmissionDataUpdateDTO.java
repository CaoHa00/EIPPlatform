package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata;

import java.util.List;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentUpdateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceUpdateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceUpdateDTO;

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
public class AirEmissionDataUpdateDTO {

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

    List<AirMonitoringExceedanceUpdateDTO> airMonitoringExceedances;

    String airAutoStationLocation;

    String airAutoStationGps;

    String airAutoStationMap;

    String airAutoSourceDesc;

    String airAutoDataFrequency;

    String airAutoParamList;

    String airAutoCalibrationInfo;

    String airAutoIncidentSummary;

    String airAutoDowntimeDesc;

    List<AirAutoMonitoringStatUpdateDTO> airAutoMonitoringStats;

    List<AirAutoMonitoringIncidentUpdateDTO> airAutoMonitoringIncidents;

    String airAutoAvgCalcDesc;

    String airAutoAvgCompareDesc;

    String airAutoExceedDaysSummary;

    String airAutoAbnormalReason;

    List<AirAutoQcvnExceedanceUpdateDTO> airAutoQcvnExceedances;

    String airAutoCompletenessReview;

    String airAutoExceedConclusion;
}