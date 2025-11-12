package com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteWaterDataUpdateDTO {
    String treatmentWwDesc;
    Double domWwCy;
    Double domWwPy;
    Double domWwDesign;
    Double industrialWwCy;
    Double industrialWwPy;
    Double industrialWwDesign;
    Double coolingWaterCy;
    Double coolingWaterPy;
    Double coolingWaterDesign;
    String connectionStatusDesc;
    String domMonitorPeriod;
    String domMonitorFreq;
    String domMonitorLocations;
    Integer domSampleCount;
    String domQcvnStandard;
    String domAgencyName;
    String domAgencyVimcerts;
    String indMonitorPeriod;
    String indMonitorFreq;
    String indMonitorLocations;
    Integer indSampleCount;
    String indQcvnStandard;
    String indAgencyName;
    String indAgencyVimcerts;
    List<WasteWaterMonitoringExceedancesUpdateDTO> monitoringExceedances;
    String autoStationLocation;
    String autoStationGps;
    String autoSourceDesc;
    String autoDataFrequency;
    String autoParamList;
    String autoCalibrationInfo;
    String autoIncidentSummary;
    String autoDowntimeDesc;
    List<AutoWWMonitoringStatsUpdateDTO> monitoringStats;
    List<AutoWWMonitoringIncidentsUpdateDTO> monitoringIncidents;
    String autoAvgCalcDesc;
    String autoAvgCompareDesc;
    String autoExceedDaysSummary;
    String autoAbnormalReason;
    List<AutoWWQcvnExceedancesUpdateDTO> qcvnExceedances;
    String autoCompletenessReview;
    String autoExceedConclusion;
}