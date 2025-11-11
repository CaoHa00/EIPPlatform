package com.EIPplatform.model.entity.report.reportB04;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class b4_part_3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "b4_part_3_id")
    Long b4Part3Id;

    @Column(name = "applied_energy_saving_models", nullable = false, columnDefinition = "NVARCHAR(255)")
    String appliedEnergySavingModels;


    // ----------------------------Electricity-----------------------------------
    @Column(name = "electricity_saving", nullable = false, columnDefinition = "NVARCHAR(255)")
    String electricitySaving;

    @Column(name = "electricity_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String electricityUnit = "kwh";

    @Column(name = "electricity_co2_reduction", nullable = false, columnDefinition = "NVARCHAR(255)")
    String electricityCo2Reduction;

    @Column(name = "electricity_saving_cost", nullable = false, columnDefinition = "NVARCHAR(255)")
    String electricitySavingCost;

    @Column(name = "electricity_other_benefits ", nullable = false, columnDefinition = "NVARCHAR(255)")
    String electricityOtherBenefits;

    // ----------------------------Fuel-----------------------------------
    @Column(name = "fuel_saving_amount", nullable = false, columnDefinition = "NVARCHAR(255)")
    String fuelSavingAmount;

    @Column(name = "fuel_saving_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String fuelSavingUnit;

    @Column(name = "fuel_co2_reduction ", nullable = false, columnDefinition = "NVARCHAR(255)")
    String fuelCo2Reduction;

    @Column(name = "fuel_saving_cost", nullable = false, columnDefinition = "NVARCHAR(255)")
    String fuelSavingCost;

    @Column(name = "fuel_other_benefits", nullable = false, columnDefinition = "NVARCHAR(255)")
    String fuelOtherBenefits;

    // ----------------------------Total CO2 reduction-----------------------------------
    @Column(name = "total_co2_reduction", nullable = false, columnDefinition = "NVARCHAR(255)")
    String totalCo2Reduction;


    // ----------------------------Water-----------------------------------
    @Column(name = "water_saving", nullable = false, columnDefinition = "NVARCHAR(255)")
    String waterSaving;

    @Column(name = "water_saving_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String waterSavingUnit = "m3/year";

    @Column(name = "water_saving_cost", nullable = false, columnDefinition = "NVARCHAR(255)")
    String waterSavingCost;



    // ----------------------------Wastewater-----------------------------------
    @Column(name = "wastewater_reduction", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wastewaterReduction;

    @Column(name = "wastewater_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wastewaterUnit = "m3/year";

    @Column(name = "wastewater_saving_cost", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wastewaterSavingCost;



    // ----------------------------Waste-----------------------------------
    @Column(name = "waste_treated_reduction", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wasteTreatedReduction;

    @Column(name = "waste_reuse_amount", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wasteReuseAmount;

    @Column(name = "waste_reuse_cost_saving", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wasteReuseCostSaving;

    @Column(name = "waste_reuse_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wasteReuseUnit = "ton/year";

    @Column(name = "waste_recycle_amount", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wasteRecycleAmount;

    @Column(name = "waste_recycle_cost_saving", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wasteRecycleCostSaving;

    @Column(name = "waste_recycle_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String wasteRecycleUnit = "ton/year";



    // ----------------------------Primary-----------------------------------
    @Column(name = "primary_material_saving_quantity", nullable = false, columnDefinition = "NVARCHAR(255)")
    String primaryMaterialSavingQuantity;

    @Column(name = "primary_material_cost_saving", nullable = false, columnDefinition = "NVARCHAR(255)")
    String primaryMaterialCostSaving;

    @Column(name = "primary_material_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String primaryMaterialUnit = "ton/year";



    // ----------------------------Chemical-----------------------------------
    @Column(name = "chemical_saving_amount", nullable = false, columnDefinition = "NVARCHAR(255)")
    String chemicalSavingAmount;

    @Column(name = "chemical_cost_saving", nullable = false, columnDefinition = "NVARCHAR(255)")
    String chemicalCostSaving;

    @Column(name = "chemical_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String chemicalUnit = "ton/year";

}
