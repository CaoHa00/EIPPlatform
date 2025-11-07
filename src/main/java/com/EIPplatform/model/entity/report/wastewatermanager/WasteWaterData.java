package com.EIPplatform.model.entity.report.wastewatermanager;

import com.EIPplatform.model.entity.report.ReportA05;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "waste_water_data", indexes = {
        @Index(name = "idx_waste_water_report_id", columnList = "report_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteWaterData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ww_id", updatable = false, nullable = false)
    Long wwId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    @JsonBackReference(value = "report-wastewater")
    ReportA05 report;

    @Column(name = "treatment_ww_desc", columnDefinition = "NVARCHAR(MAX)")
    String treatmentWwDesc;

    @Column(name = "dom_ww_cy", nullable = false)
    Double domWwCy;

    @Column(name = "dom_ww_py", nullable = false)
    Double domWwPy;

    @Column(name = "dom_ww_design", nullable = false)
    Double domWwDesign;

    @Column(name = "industrial_ww_cy", nullable = false)
    Double industrialWwCy;

    @Column(name = "industrial_ww_py", nullable = false)
    Double industrialWwPy;

    @Column(name = "industrial_ww_design", nullable = false)
    Double industrialWwDesign;

    @Column(name = "cooling_water_cy")
    Double coolingWaterCy;

    @Column(name = "cooling_water_py")
    Double coolingWaterPy;

    @Column(name = "cooling_water_design")
    Double coolingWaterDesign;

    @Column(name = "connection_status_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String connectionStatusDesc;

    @Column(name = "connection_diagram", columnDefinition = "NVARCHAR(255)")
    String connectionDiagram;

    @Column(name = "dom_monitor_period", columnDefinition = "NVARCHAR(100)", nullable = false)
    String domMonitorPeriod;

    @Column(name = "dom_monitor_freq", columnDefinition = "NVARCHAR(100)", nullable = false)
    String domMonitorFreq;

    @Column(name = "dom_monitor_locations", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String domMonitorLocations;

    @Column(name = "dom_sample_count", nullable = false)
    Integer domSampleCount;

    @Column(name = "dom_qcvn_standard", columnDefinition = "NVARCHAR(100)", nullable = false)
    String domQcvnStandard;

    @Column(name = "dom_agency_name", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String domAgencyName;

    @Column(name = "dom_agency_vimcerts", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String domAgencyVimcerts;

    @Column(name = "ind_monitor_period", columnDefinition = "NVARCHAR(100)", nullable = false)
    String indMonitorPeriod;

    @Column(name = "ind_monitor_freq", columnDefinition = "NVARCHAR(100)", nullable = false)
    String indMonitorFreq;

    @Column(name = "ind_monitor_locations", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String indMonitorLocations;

    @Column(name = "ind_sample_count", nullable = false)
    Integer indSampleCount;

    @Column(name = "ind_qcvn_standard", columnDefinition = "NVARCHAR(100)", nullable = false)
    String indQcvnStandard;

    @Column(name = "ind_agency_name", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String indAgencyName;

    @Column(name = "ind_agency_vimcerts", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String indAgencyVimcerts;

    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<WasteWaterMonitoringExceedances> monitoringExceedances = new ArrayList<>();

    @Column(name = "auto_station_location", columnDefinition = "NVARCHAR(500)", nullable = false)
    String autoStationLocation;

    @Column(name = "auto_station_gps", columnDefinition = "NVARCHAR(30)")
    String autoStationGps;

    @Column(name = "auto_station_map", columnDefinition = "NVARCHAR(255)")
    String autoStationMap;

    @Column(name = "auto_source_desc", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoSourceDesc;

    @Column(name = "auto_data_frequency", columnDefinition = "NVARCHAR(100)", nullable = false)
    String autoDataFrequency;

    @Column(name = "auto_calibration_info", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoCalibrationInfo;

    @Column(name = "auto_incident_summary", columnDefinition = "NVARCHAR(MAX)")
    String autoIncidentSummary;

    @Column(name = "auto_downtime_desc", columnDefinition = "NVARCHAR(MAX)")
    String autoDowntimeDesc;

    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<AutoWWMonitoringStats> monitoringStats = new ArrayList<>();

    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<AutoWWMonitoringIncidents> monitoringIncidents = new ArrayList<>();

    @Column(name = "auto_exceed_days_summary", columnDefinition = "NVARCHAR(MAX)")
    String autoExceedDaysSummary;

    @Column(name = "auto_abnormal_reason", columnDefinition = "NVARCHAR(MAX)")
    String autoAbnormalReason;

    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<AutoWWQcvnExceedances> qcvnExceedances = new ArrayList<>();

    @Column(name = "auto_completeness_review", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoCompletenessReview;

    @Column(name = "auto_exceed_summary", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoExceedSummary;
}
