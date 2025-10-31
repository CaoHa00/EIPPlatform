package com.EIPplatform.model.entity.report.wastemanagement;

import com.EIPplatform.model.entity.report.ReportA05;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "waste_management_data", indexes = {
        @Index(name = "idx_waste_management_report_id", columnList = "report_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteManagementData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wm_id", updatable = false, nullable = false)
    Long wmId;

    // ============= QUAN HỆ ONE-TO-ONE VỚI REPORT =============
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    @JsonBackReference(value = "report-waste-management")
    ReportA05 report;

    // ============= QUAN HỆ CHUNG CHO CHẤT THẢI RẮN THÔNG THƯỜNG =============
    @Column(name = "sw_general_note", columnDefinition = "TEXT")
    String swGeneralNote; // Ghi chú thống kê chất thải phát sinh (không bắt buộc)

    // ============= BẢNG 3.1: THỐNG KÊ CTRSH (SINH HOẠT) - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<DomesticSolidWasteStat> domesticSolidWasteStats; // Dynamic: Mỗi item là một dòng

    // ============= BẢNG 3.2: THỐNG KÊ CTRCNTT (CÔNG NGHIỆP) - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<IndustrialSolidWasteStat> industrialSolidWasteStats; // Dynamic: Mỗi item là một dòng

    // ============= BẢNG 3.3: THỐNG KÊ TÁI SỬ DỤNG CTRCNTT - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<RecycleIndustrialWasteStat> recycleIndustrialWasteStats; // Dynamic: Mỗi item là một dòng

    // ============= BẢNG 3.4: THỐNG KÊ CTRTT KHÁC - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<OtherSolidWasteStat> otherSolidWasteStats; // Dynamic: Mỗi item là một dòng

    // ============= BẢNG 4.1: THỐNG KÊ CTNH (THƯỜNG XUYÊN/ĐỘT XUẤT) - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<HazardousWasteStat> hazardousWasteStats; // Dynamic: Mỗi item là một dòng

    // ============= BẢNG 4.2: THỐNG KÊ CTNH XUẤT KHẨU - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<ExportedHwStat> exportedHwStats; // Dynamic: Mỗi item là một dòng

    // ============= BẢNG 4.3: THỐNG KÊ CTNH TỰ XỬ LÝ - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<SelfTreatedHwStat> selfTreatedHwStats; // Dynamic: Mỗi item là một dòng

    // ============= PHÒNG NGỪA, ỨNG PHÓ SỰ CỐ MÔI TRƯỜNG =============
    @Column(name = "incident_plan_development", columnDefinition = "TEXT", nullable = false)
    String incidentPlanDevelopment; // 6.1. Kế hoạch phòng ngừa, ứng phó (bắt buộc)

    @Column(name = "incident_prevention_measures", columnDefinition = "TEXT", nullable = false)
    String incidentPreventionMeasures; // 6.2. Giải pháp phòng ngừa (bắt buộc)

    @Column(name = "incident_response_report", columnDefinition = "TEXT", nullable = false)
    String incidentResponseReport; // 6.2. Ứng phó và khắc phục sự cố (bắt buộc)

    // ============= BẢNG 7.1: POP INVENTORY - DYNAMIC LIST =============
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<PopInventoryStat> popInventoryStats; // Dynamic: Mỗi item là một dòng

    // ============= BẢNG 7.2: POP EMISSION - FIXED ROWS (8 CATEGORIES) - FIELDS RIÊNG =============
    // Category 1: Water
    @Column(name = "water_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal waterTotalVolumeKg; // Phát thải vào nước (≥0)

    @Column(name = "water_estimation_method", columnDefinition = "TEXT", nullable = false)
    String waterEstimationMethod; // Phương pháp ước tính cho nước

    // Category 2: Air
    @Column(name = "air_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal airTotalVolumeKg; // Phát thải vào không khí (≥0)

    @Column(name = "air_estimation_method", columnDefinition = "TEXT", nullable = false)
    String airEstimationMethod; // Phương pháp cho không khí

    // Category 3: Soil
    @Column(name = "soil_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal soilTotalVolumeKg; // Phát thải vào đất (≥0)

    @Column(name = "soil_estimation_method", columnDefinition = "TEXT", nullable = false)
    String soilEstimationMethod; // Phương pháp cho đất

    // Category 4: Sewage Sludge
    @Column(name = "sewage_sludge_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal sewageSludgeTotalVolumeKg; // Chuyển giao vào bùn thải (≥0)

    @Column(name = "sewage_sludge_estimation_method", columnDefinition = "TEXT", nullable = false)
    String sewageSludgeEstimationMethod; // Phương pháp cho bùn thải

    // Category 5.1: HW Onsite
    @Column(name = "hw_onsite_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal hwOnsiteTotalVolumeKg; // Chuyển giao CTNH trong cơ sở (≥0)

    @Column(name = "hw_onsite_estimation_method", columnDefinition = "TEXT", nullable = false)
    String hwOnsiteEstimationMethod; // Phương pháp cho HW onsite

    // Category 5.2: HW Recycle
    @Column(name = "hw_recycle_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal hwRecycleTotalVolumeKg; // Chuyển giao CTNH tái chế (≥0)

    @Column(name = "hw_recycle_estimation_method", columnDefinition = "TEXT", nullable = false)
    String hwRecycleEstimationMethod; // Phương pháp cho HW recycle

    // Category 5.3: HW Disposal
    @Column(name = "hw_disposal_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal hwDisposalTotalVolumeKg; // Chuyển giao CTNH thải bỏ (≥0)

    @Column(name = "hw_disposal_estimation_method", columnDefinition = "TEXT", nullable = false)
    String hwDisposalEstimationMethod; // Phương pháp cho HW disposal
}