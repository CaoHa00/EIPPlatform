package com.EIPplatform.model.entity.report.wastemanagement;

import com.EIPplatform.model.entity.report.ReportA05;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    @JsonManagedReference
    @Builder.Default
    List<DomesticSolidWasteStat> domesticSolidWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Builder.Default
    List<IndustrialSolidWasteStat> industrialSolidWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Builder.Default
    List<RecycleIndustrialWasteStat> recycleIndustrialWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Builder.Default
    List<OtherSolidWasteStat> otherSolidWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Builder.Default
    List<HazardousWasteStat> hazardousWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Builder.Default
    List<ExportedHwStat> exportedHwStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Builder.Default
    List<SelfTreatedHwStat> selfTreatedHwStats = new ArrayList<>();

    @Column(name = "incident_plan_development", columnDefinition = "TEXT", nullable = false)
    String incidentPlanDevelopment;

    @Column(name = "incident_prevention_measures", columnDefinition = "TEXT", nullable = false)
    String incidentPreventionMeasures;

    @Column(name = "incident_response_report", columnDefinition = "TEXT", nullable = false)
    String incidentResponseReport;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Builder.Default
    List<PopInventoryStat> popInventoryStats = new ArrayList<>();

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
