package com.EIPplatform.model.entity.report.report05.wastemanagement;

import com.EIPplatform.model.entity.report.report05.ReportA05;
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
    
    @Column(name = "sw_general_note", columnDefinition = "NVARCHAR(MAX)")
    String swGeneralNote;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<DomesticSolidWasteStat> domesticSolidWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<IndustrialSolidWasteStat> industrialSolidWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<RecycleIndustrialWasteStat> recycleIndustrialWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<OtherSolidWasteStat> otherSolidWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<HazardousWasteStat> hazardousWasteStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<ExportedHwStat> exportedHwStats = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<SelfTreatedHwStat> selfTreatedHwStats = new ArrayList<>();
    
    @Column(name = "incident_plan_development", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String incidentPlanDevelopment;
    
    @Column(name = "incident_prevention_measures", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String incidentPreventionMeasures;
    
    @Column(name = "incident_response_report", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String incidentResponseReport;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "wasteManagementData")
    @JsonManagedReference
    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    List<PopInventoryStat> popInventoryStats = new ArrayList<>();

    @Column(name = "water_total_volume")
    Double waterTotalVolume;

    @Column(name = "water_total_unit")
    String waterTotalUnit;

    @Column(name = "water_estimation_method", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String waterEstimationMethod;

    @Column(name = "air_total_volume")
    Double airTotalVolume;

    @Column(name = "air_total_unit")
    String airTotalUnit;
    
    @Column(name = "air_estimation_method", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String airEstimationMethod;

    @Column(name = "soil_total_volume")
    Double soilTotalVolume;

    @Column(name = "soil_total_unit")
    String soilTotalUnit;
    
    @Column(name = "soil_estimation_method", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String soilEstimationMethod;

    @Column(name = "sewage_sludge_total_volume")
    Double sewageSludgeTotalVolume;

    @Column(name = "sewage_sludge_total_unit")
    String sewageSludgeTotalUnit;
    
    @Column(name = "sewage_sludge_estimation_method", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String sewageSludgeEstimationMethod;

    @Column(name = "hw_onsite_total_volume")
    Double hwOnsiteTotalVolume;

    @Column(name = "hw_onsite_total_unit")
    String hwOnsiteTotalUnit;
    
    @Column(name = "hw_onsite_estimation_method", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String hwOnsiteEstimationMethod;
    
    @Column(name = "hw_recycle_total_volume")
    Double hwRecycleTotalVolume;

    @Column(name = "hw_recycle_total_unit")
    String hwRecycleTotalUnit;
    
    @Column(name = "hw_recycle_estimation_method", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String hwRecycleEstimationMethod;

    @Column(name = "hw_disposal_total_volume")
    Double hwDisposalTotalVolume;

    @Column(name = "hw_disposal_total_unit")
    String hwDisposalTotalUnit;
    
    @Column(name = "hw_disposal_estimation_method", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    String hwDisposalEstimationMethod;
}
