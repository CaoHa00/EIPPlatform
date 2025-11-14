package com.EIPplatform.model.entity.report.report05.airemmissionmanagement;

import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.Double;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "air_emission_data")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirEmissionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "air_emission_data_id")
    Long airEmissionDataId;

    @OneToOne
    @JoinColumn(name = "report_id", nullable = false)
    ReportA05 report;
    
    @Lob
    @Column(name = "air_treatment_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airTreatmentDesc;

    @Column(name = "air_emission_cy", nullable = false)
    Double airEmissionCy;

    @Column(name = "air_emission_py", nullable = false)
    Double airEmissionPy;
    
    @Column(name = "air_monitor_period", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airMonitorPeriod;
    
    @Column(name = "air_monitor_freq", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airMonitorFreq;
    
    @Lob
    @Column(name = "air_monitor_locations", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airMonitorLocations;

    @Column(name = "air_sample_count", nullable = false)
    Integer airSampleCount;
    
    @Column(name = "air_qcvn_standard", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airQcvnStandard;
    
    @Column(name = "air_agency_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String airAgencyName;
    
    @Column(name = "air_agency_vimcerts", nullable = false, columnDefinition = "NVARCHAR(50)")
    String airAgencyVimcerts;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    List<AirMonitoringExceedance> airMonitoringExceedances = new ArrayList<>();
    
    @Column(name = "air_auto_station_location", nullable = false, columnDefinition = "NVARCHAR(500)")
    String airAutoStationLocation;
    
    @Column(name = "air_auto_station_gps", columnDefinition = "NVARCHAR(30)")
    String airAutoStationGps;
    
    @Column(name = "air_auto_station_map", columnDefinition = "NVARCHAR(255)")
    String airAutoStationMapFilePath;
    
    @Lob
    @Column(name = "air_auto_source_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoSourceDesc;
    
    @Column(name = "air_auto_data_frequency", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airAutoDataFrequency;
    
    @Lob
    @Column(name = "air_auto_param_list", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoParamList;
    
    @Lob
    @Column(name = "air_auto_calibration_info", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoCalibrationInfo;
    
    @Lob
    @Column(name = "air_auto_incident_summary", columnDefinition = "NVARCHAR(MAX)")
    String airAutoIncidentSummary;
    
    @Lob
    @Column(name = "air_auto_downtime_desc", columnDefinition = "NVARCHAR(MAX)")
    String airAutoDowntimeDesc;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    List<AirAutoMonitoringStat> airAutoMonitoringStats = new ArrayList<>();

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    List<AirAutoMonitoringIncident> airAutoMonitoringIncidents = new ArrayList<>();
    
    @Lob
    @Column(name = "air_auto_avg_calc_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoAvgCalcDesc;
    
    @Lob
    @Column(name = "air_auto_avg_compare_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoAvgCompareDesc;
    
    @Lob
    @Column(name = "air_auto_exceed_days_summary", columnDefinition = "NVARCHAR(MAX)")
    String airAutoExceedDaysSummary;
    
    @Lob
    @Column(name = "air_auto_abnormal_reason", columnDefinition = "NVARCHAR(MAX)")
    String airAutoAbnormalReason;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    List<AirAutoQcvnExceedance> airAutoQcvnExceedances = new ArrayList<>();
    
    @Lob
    @Column(name = "air_auto_completeness_review", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoCompletenessReview;
    
    @Lob
    @Column(name = "air_auto_exceed_conclusion", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoExceedConclusion;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
