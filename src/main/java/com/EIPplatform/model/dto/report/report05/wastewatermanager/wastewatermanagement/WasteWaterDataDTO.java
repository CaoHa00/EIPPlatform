package com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    String connectionDiagram;
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
    String autoStationMap;
    String autoSourceDesc;
    String autoDataFrequency;
    String autoCalibrationInfo;
    String autoIncidentSummary;
    String autoDowntimeDesc;
    List<AutoWWMonitoringStatsDTO> monitoringStats;
    List<AutoWWMonitoringIncidentsDTO> monitoringIncidents;
    String autoExceedDaysSummary;
    String autoAbnormalReason;
    List<AutoWWQcvnExceedancesDTO> qcvnExceedances;
    String autoCompletenessReview;
    String autoExceedSummary;
}