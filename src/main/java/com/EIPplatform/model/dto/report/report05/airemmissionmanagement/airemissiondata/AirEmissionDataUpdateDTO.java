package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentUpdateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceUpdateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceUpdateDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirEmissionDataUpdateDTO {

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