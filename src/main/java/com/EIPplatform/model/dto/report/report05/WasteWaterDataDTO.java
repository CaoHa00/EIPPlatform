package com.EIPplatform.model.dto.report.report05;//package com.EIPplatform.model.dto.report.report;
//
//import com.EIPplatform.model.dto.report.report.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsDTO;
//import com.EIPplatform.model.dto.report.report.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsDTO;
//import com.EIPplatform.model.dto.report.report.wastewatermanager.autowwqcvnexceedances.AutoWWQcvnExceedancesDTO;
//import com.EIPplatform.model.dto.report.report.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class WasteWaterDataDTO {
//
//    Long wwId;
//
//    // Nước thải sinh hoạt
//    String treatmentWwDesc;
//    Double domWwCy;
//    Double domWwPy;
//    Double domWwDesign;
//
//    // Nước thải công nghiệp
//    Double industrialWwCy;
//    Double industrialWwPy;
//    Double industrialWwDesign;
//
//    // Nước làm mát
//    Double coolingWaterCy;
//    Double coolingWaterPy;
//    Double coolingWaterDesign;
//
//    // Kết nối hệ thống xử lý tập trung
//    String connectionStatusDesc;
//    String connectionDiagram;
//
//    // Kết quả quan trắc - Nước thải sinh hoạt
//    String domMonitorPeriod;
//    String domMonitorFreq;
//    String domMonitorLocations;
//    Integer domSampleCount;
//    String domQcvnStandard;
//    String domAgencyName;
//    String domAgencyVimcerts;
//
//    // Kết quả quan trắc - Nước thải công nghiệp
//    String indMonitorPeriod;
//    String indMonitorFreq;
//    String indMonitorLocations;
//    Integer indSampleCount;
//    String indQcvnStandard;
//    String indAgencyName;
//    String indAgencyVimcerts;
//
//    // Bảng 1.1, 1.2
//    @Builder.Default
//    List<WasteWaterMonitoringExceedancesDTO> monitoringExceedances = new ArrayList<>();
//
//    // Quan trắc tự động - Thông tin chung
//    String autoStationLocation;
//    String autoStationGps;
//    String autoStationMap;
//    String autoSourceDesc;
//    String autoDataFrequency;
//    String autoCalibrationInfo;
//
//    // Tình trạng hoạt động
//    String autoIncidentSummary;
//    String autoDowntimeDesc;
//
//    // Bảng 1.3
//    @Builder.Default
//    List<AutoWWMonitoringStatsDTO> monitoringStats = new ArrayList<>();
//
//    // Bảng 1.4
//    @Builder.Default
//    List<AutoWWMonitoringIncidentsDTO> monitoringIncidents = new ArrayList<>();
//
//    // Nhận xét
//    String autoExceedDaysSummary;
//    String autoAbnormalReason;
//
//    // Bảng 1.5
//    @Builder.Default
//    List<AutoWWQcvnExceedancesDTO> qcvnExceedances = new ArrayList<>();
//
//    // Kết luận
//    String autoCompletenessReview;
//    String autoExceedSummary;
//}