package com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement;

import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteWaterDataDTO {
    Long wwId;
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
    String connectionDiagram; // File path
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
    List<WasteWaterMonitoringExceedancesDTO> monitoringExceedances;
    String autoStationLocation;
    String autoStationGps;
    String autoStationMap; // File path
    String autoSourceDesc;
    String autoDataFrequency;
    String autoParamList;
    String autoCalibrationInfo;
    String autoIncidentSummary;
    String autoDowntimeDesc;
    List<AutoWWMonitoringStatsDTO> monitoringStats;
    List<AutoWWMonitoringIncidentsDTO> monitoringIncidents;
    String autoAvgCalcDesc;
    String autoAvgCompareDesc;
    String autoExceedDaysSummary;
    String autoAbnormalReason;
    List<AutoWWQcvnExceedancesDTO> qcvnExceedances;
    String autoCompletenessReview;
    String autoExceedSummary;
    String autoExceedConclusion;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}