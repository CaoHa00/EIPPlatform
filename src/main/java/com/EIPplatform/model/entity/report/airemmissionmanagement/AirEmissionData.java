package com.EIPplatform.model.entity.report.airemmissionmanagement;

import com.EIPplatform.model.entity.report.ReportA05;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
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

    // 2.1. Xử lý khí thải
    @Lob
    @Column(name = "air_treatment_desc", nullable = false)
    String airTreatmentDesc;

    @Column(name = "air_emission_cy", precision = 15, scale = 2, nullable = false)
    BigDecimal airEmissionCy;

    @Column(name = "air_emission_py", precision = 15, scale = 2, nullable = false)
    BigDecimal airEmissionPy;

    // 2.2.1. Kết quả quan trắc định kỳ
    @Column(name = "air_monitor_period", length = 100, nullable = false)
    String airMonitorPeriod;

    @Column(name = "air_monitor_freq", length = 100, nullable = false)
    String airMonitorFreq;

    @Lob
    @Column(name = "air_monitor_locations", nullable = false)
    String airMonitorLocations;

    @Column(name = "air_sample_count", nullable = false)
    Integer airSampleCount;

    @Column(name = "air_qcvn_standard", length = 100, nullable = false)
    String airQcvnStandard;

    @Column(name = "air_agency_name", length = 255, nullable = false)
    String airAgencyName;

    @Column(name = "air_agency_vimcerts", length = 50, nullable = false)
    String airAgencyVimcerts;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    List<AirMonitoringExceedance> airMonitoringExceedances = new ArrayList<>();

    // 2.2.2: Quan trắc khí thải tự động - a) Thông tin chung
    @Column(name = "air_auto_station_location", length = 500, nullable = false)
    String airAutoStationLocation;

    @Column(name = "air_auto_station_gps", length = 30)
    String airAutoStationGps;

    @Column(name = "air_auto_station_map", length = 255)
    String airAutoStationMapFilePath;

    @Lob
    @Column(name = "air_auto_source_desc", nullable = false)
    String airAutoSourceDesc;

    @Column(name = "air_auto_data_frequency", length = 100, nullable = false)
    String airAutoDataFrequency;

    @Lob
    @Column(name = "air_auto_param_list", nullable = false)
    String airAutoParamList;

    @Lob
    @Column(name = "air_auto_calibration_info", nullable = false)
    String airAutoCalibrationInfo;

    @Lob
    @Column(name = "air_auto_incident_summary")
    String airAutoIncidentSummary;

    @Lob
    @Column(name = "air_auto_downtime_desc")
    String airAutoDowntimeDesc;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    List<AirAutoMonitoringStat> airAutoMonitoringStats = new ArrayList<>();

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    List<AirAutoMonitoringIncident> airAutoMonitoringIncidents = new ArrayList<>();

    // c) Nhận xét kết quả quan trắc
    @Lob
    @Column(name = "air_auto_avg_calc_desc", nullable = false)
    String airAutoAvgCalcDesc;

    @Lob
    @Column(name = "air_auto_avg_compare_desc", nullable = false)
    String airAutoAvgCompareDesc;

    @Lob
    @Column(name = "air_auto_exceed_days_summary")
    String airAutoExceedDaysSummary;

    @Lob
    @Column(name = "air_auto_abnormal_reason")
    String airAutoAbnormalReason;

    @OneToMany(mappedBy = "airEmissionData", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    List<AirAutoQcvnExceedance> airAutoQcvnExceedances = new ArrayList<>();

    // d) Kết luận
    @Lob
    @Column(name = "air_auto_completeness_review", nullable = false)
    String airAutoCompletenessReview;

    @Lob
    @Column(name = "air_auto_exceed_conclusion", nullable = false)
    String airAutoExceedConclusion;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
