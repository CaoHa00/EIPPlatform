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

    // ============= QUAN HỆ ONE-TO-ONE VỚI REPORT_A05 =============
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    @JsonBackReference(value = "report-wastewater")
    ReportA05 report;

    // ============= NƯỚC THẢI SINH HOẠT =============
    @Column(name = "treatment_ww_desc", columnDefinition = "NVARCHAR(MAX)")
    String treatmentWwDesc;

    @Column(name = "dom_ww_cy", nullable = false)
    Double domWwCy;

    @Column(name = "dom_ww_py", nullable = false)
    Double domWwPy;

    @Column(name = "dom_ww_design", nullable = false)
    Double domWwDesign;

    // ============= NƯỚC THẢI CÔNG NGHIỆP =============
    @Column(name = "industrial_ww_cy", nullable = false)
    Double industrialWwCy;

    @Column(name = "industrial_ww_py", nullable = false)
    Double industrialWwPy;

    @Column(name = "industrial_ww_design", nullable = false)
    Double industrialWwDesign;

    // ============= NƯỚC LÀM MÁT =============
    @Column(name = "cooling_water_cy")
    Double coolingWaterCy;

    @Column(name = "cooling_water_py")
    Double coolingWaterPy;

    @Column(name = "cooling_water_design")
    Double coolingWaterDesign;

    // ============= TÌNH HÌNH ĐẤU NỐI HỆ THỐNG XLNT TẬP TRUNG =============
    @Column(name = "connection_status_desc", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    String connectionStatusDesc;

    @Column(name = "connection_diagram")

    String connectionDiagram;// file

    // ============= KẾT QUẢ QUAN TRẮC NƯỚC THẢI ĐẦU RA =============
    // Nước thải sinh hoạt
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

    // Nước thải công nghiệp
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

    // ============= BẢNG 1.1, 1.2: THỐNG KÊ VỊ TRÍ & KẾT QUẢ VƯỢT QCVN =============
    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<WasteWaterMonitoringExceedances> monitoringExceedances = new ArrayList<>();

    // ============= QUAN TRẮC NƯỚC THẢI LIÊN TỤC TỰ ĐỘNG =============
    // a) Thông tin chung về hệ thống
    @Column(name = "auto_station_location", columnDefinition = "NVARCHAR(500)", nullable = false)
    String autoStationLocation;

    @Column(name = "auto_station_gps", length = 30, nullable = true)
    String autoStationGps;

    @Column(name = "auto_station_map", columnDefinition = "NVARCHAR(255)", nullable = true)
    String autoStationMap; // File path/URL

    @Column(name = "auto_source_desc", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoSourceDesc;

    @Column(name = "auto_data_frequency", columnDefinition = "NVARCHAR(100)", nullable = false)
    String autoDataFrequency;

    @Column(name = "auto_calibration_info", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoCalibrationInfo;

    // b) Tình trạng hoạt động của trạm
    @Column(name = "auto_incident_summary", columnDefinition = "NVARCHAR(MAX)", nullable = true)
    String autoIncidentSummary;

    @Column(name = "auto_downtime_desc", columnDefinition = "NVARCHAR(MAX)", nullable = true)
    String autoDowntimeDesc;

    // ============= BẢNG 1.3: THỐNG KÊ SỐ LIỆU QUAN TRẮC =============
    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<AutoWWMonitoringStats> monitoringStats = new ArrayList<>();

    // ============= BẢNG 1.4: THỐNG KÊ CÁC SỰ CỐ TẠI TRẠM =============
    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<AutoWWMonitoringIncidents> monitoringIncidents = new ArrayList<>();

    // c) Nhận xét kết quả quan trắc
    @Column(name = "auto_exceed_days_summary", columnDefinition = "NVARCHAR(MAX)", nullable = true)
    String autoExceedDaysSummary;

    @Column(name = "auto_abnormal_reason", columnDefinition = "NVARCHAR(MAX)", nullable = true)
    String autoAbnormalReason;

    // ============= BẢNG 1.5: THỐNG KÊ VƯỢT QCVN =============
    @OneToMany(mappedBy = "wasteWaterData", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<AutoWWQcvnExceedances> qcvnExceedances = new ArrayList<>();

    // d) Kết luận
    @Column(name = "auto_completeness_review", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoCompletenessReview;

    @Column(name = "auto_exceed_summary", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String autoExceedSummary;
}
