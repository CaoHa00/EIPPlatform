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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false, unique = true)
    @JsonBackReference(value = "report-waste-management")
    ReportA05 report;

    @Column(name = "sw_general_note", columnDefinition = "TEXT")
    String swGeneralNote;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<DomesticSolidWasteStat> domesticSolidWasteStats;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<IndustrialSolidWasteStat> industrialSolidWasteStats;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<RecycleIndustrialWasteStat> recycleIndustrialWasteStats;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<OtherSolidWasteStat> otherSolidWasteStats;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<HazardousWasteStat> hazardousWasteStats;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<ExportedHwStat> exportedHwStats;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<SelfTreatedHwStat> selfTreatedHwStats;

    @Column(name = "incident_plan_development", columnDefinition = "TEXT", nullable = false)
    String incidentPlanDevelopment;

    @Column(name = "incident_prevention_measures", columnDefinition = "TEXT", nullable = false)
    String incidentPreventionMeasures;

    @Column(name = "incident_response_report", columnDefinition = "TEXT", nullable = false)
    String incidentResponseReport;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    List<PopInventoryStat> popInventoryStats;

    @Column(name = "water_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal waterTotalVolumeKg;

    @Column(name = "water_estimation_method", columnDefinition = "TEXT", nullable = false)
    String waterEstimationMethod;

    @Column(name = "air_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal airTotalVolumeKg;

    @Column(name = "air_estimation_method", columnDefinition = "TEXT", nullable = false)
    String airEstimationMethod;

    @Column(name = "soil_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal soilTotalVolumeKg;
    @Column(name = "soil_estimation_method", columnDefinition = "TEXT", nullable = false)
    String soilEstimationMethod;

    @Column(name = "sewage_sludge_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal sewageSludgeTotalVolumeKg;

    @Column(name = "sewage_sludge_estimation_method", columnDefinition = "TEXT", nullable = false)
    String sewageSludgeEstimationMethod;

    @Column(name = "hw_onsite_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal hwOnsiteTotalVolumeKg;

    @Column(name = "hw_onsite_estimation_method", columnDefinition = "TEXT", nullable = false)
    String hwOnsiteEstimationMethod;

    @Column(name = "hw_recycle_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal hwRecycleTotalVolumeKg;

    @Column(name = "hw_recycle_estimation_method", columnDefinition = "TEXT", nullable = false)
    String hwRecycleEstimationMethod;

    @Column(name = "hw_disposal_total_volume_kg", nullable = false, precision = 10, scale = 2)
    BigDecimal hwDisposalTotalVolumeKg;

    @Column(name = "hw_disposal_estimation_method", columnDefinition = "TEXT", nullable = false)
    String hwDisposalEstimationMethod;
}