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

import java.math.BigDecimal;
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
    @Nationalized
    @Lob
    @Column(name = "air_treatment_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airTreatmentDesc;

    @Column(name = "air_emission_cy", precision = 15, scale = 2, nullable = false)
    BigDecimal airEmissionCy;

    @Column(name = "air_emission_py", precision = 15, scale = 2, nullable = false)
    BigDecimal airEmissionPy;
    @Nationalized
    @Column(name = "air_monitor_period", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airMonitorPeriod;
    @Nationalized
    @Column(name = "air_monitor_freq", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airMonitorFreq;
    @Nationalized
    @Lob
    @Column(name = "air_monitor_locations", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airMonitorLocations;

    @Column(name = "air_sample_count", nullable = false)
    Integer airSampleCount;
    @Nationalized
    @Column(name = "air_qcvn_standard", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airQcvnStandard;
    @Nationalized
    @Column(name = "air_agency_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String airAgencyName;
    @Nationalized
    @Column(name = "air_agency_vimcerts", nullable = false, columnDefinition = "NVARCHAR(50)")
    String airAgencyVimcerts;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    List<AirMonitoringExceedance> airMonitoringExceedances = new ArrayList<>();
    @Nationalized
    @Column(name = "air_auto_station_location", nullable = false, columnDefinition = "NVARCHAR(500)")
    String airAutoStationLocation;
    @Nationalized
    @Column(name = "air_auto_station_gps", columnDefinition = "NVARCHAR(30)")
    String airAutoStationGps;
    @Nationalized
    @Column(name = "air_auto_station_map", columnDefinition = "NVARCHAR(255)")
    String airAutoStationMapFilePath;
    @Nationalized
    @Lob
    @Column(name = "air_auto_source_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoSourceDesc;
    @Nationalized
    @Column(name = "air_auto_data_frequency", nullable = false, columnDefinition = "NVARCHAR(100)")
    String airAutoDataFrequency;
    @Nationalized
    @Lob
    @Column(name = "air_auto_param_list", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoParamList;
    @Nationalized
    @Lob
    @Column(name = "air_auto_calibration_info", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoCalibrationInfo;
    @Nationalized
    @Lob
    @Column(name = "air_auto_incident_summary", columnDefinition = "NVARCHAR(MAX)")
    String airAutoIncidentSummary;
    @Nationalized
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
    @Nationalized
    @Lob
    @Column(name = "air_auto_avg_calc_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoAvgCalcDesc;
    @Nationalized
    @Lob
    @Column(name = "air_auto_avg_compare_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoAvgCompareDesc;
    @Nationalized
    @Lob
    @Column(name = "air_auto_exceed_days_summary", columnDefinition = "NVARCHAR(MAX)")
    String airAutoExceedDaysSummary;
    @Nationalized
    @Lob
    @Column(name = "air_auto_abnormal_reason", columnDefinition = "NVARCHAR(MAX)")
    String airAutoAbnormalReason;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @JsonManagedReference
    List<AirAutoQcvnExceedance> airAutoQcvnExceedances = new ArrayList<>();
    @Nationalized
    @Lob
    @Column(name = "air_auto_completeness_review", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String airAutoCompletenessReview;
    @Nationalized
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
