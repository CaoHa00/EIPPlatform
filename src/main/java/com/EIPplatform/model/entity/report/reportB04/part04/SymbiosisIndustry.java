package com.EIPplatform.model.entity.report.reportB04.part04;

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
public class SymbiosisIndustry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "si_id")
    Long siId;

    //------------------------Indus------------------------------
    @Column(name = "indus_sym_count", columnDefinition = "NVARCHAR(255)")
    String indusSymCount;

    @Column(name = "indus_sym_type", columnDefinition = "NVARCHAR(255)")
    String indusSymType;


    //------------------------is_sym------------------------------
    @Column(name = "is_sym_io_exchange")
    Boolean isSymIoExchange;

    @Column(name = "is_sym_shared_infrastructure")
    Boolean isSymSharedInfrastructure;

    @Column(name = "is_sym_type_shared_services")
    Boolean isSymTypeSharedServices;

    @Column(name = "is_sym_resource_recovery")
    Boolean isSymResourceRecovery;

    //------------------------symbiosis------------------------------
    @Column(name = "symbiosis_type_other", columnDefinition = "NVARCHAR(255)")
    String symbiosisTypeOther;

    @Column(name = "symbiosis_desc_other", columnDefinition = "NVARCHAR(255)")
    String symbiosisDescOther;



    //------------------------sym_companies------------------------------
    @Column(name = "sym_companies", columnDefinition = "NVARCHAR(255)")
    String symCompanies;


    //---------------------sym_electricity---------------------
    @Column(name = "sym_electricity_saving", columnDefinition = "NVARCHAR(255)")
    String symElectricitySaving;

    @Column(name = "sym_electricity_unit", columnDefinition = "NVARCHAR(255)")
    String symElectricityUnit = "kwh";

    @Column(name = "sym_electricity_co2_reduction", columnDefinition = "NVARCHAR(255)")
    String symElectricityCo2Reduction;

    @Column(name = "sym_electricity_saving_cost", columnDefinition = "NVARCHAR(255)")
    String symElectricitySavingCost;

    @Column(name = "sym_electricity_other_benefits", columnDefinition = "NVARCHAR(255)")
    String symElectricityOtherBenefits;

    //---------------------sym_fuel---------------------
    @Column(name = "sym_fuel_saving_amount", nullable = false, columnDefinition = "NVARCHAR(255)")
    String symFuelSavingAmount;

    @Column(name = "sym_fuel_saving_unit", nullable = false, columnDefinition = "NVARCHAR(255)")
    String symFuelSavingUnit;

    @Column(name = "sym_fuel_co2_reduction", nullable = false, columnDefinition = "NVARCHAR(255)")
    String symFuelCo2Reduction;

    @Column(name = "sym_fuel_saving_cost", nullable = false, columnDefinition = "NVARCHAR(255)")
    String symFuelSavingCost;

    @Column(name = "sym_fuel_other_benefits", nullable = false, columnDefinition = "NVARCHAR(255)")
    String symFuelOtherBenefits;

    //---------------------Total---------------------
    @Column(name = "sym_total_co2_reduction", nullable = false, columnDefinition = "NVARCHAR(255)")
    String symTotalCo2Reduction;


    //---------------------sym_water---------------------
    @Column(name = "sym_water_saving", columnDefinition = "NVARCHAR(255)")
    String symWaterSaving;

    @Column(name = "sym_water_saving_unit", columnDefinition = "NVARCHAR(255)")
    String symWaterSavingUnit = "m3/year";

    @Column(name = "sym_water_saving_cost", columnDefinition = "NVARCHAR(255)")
    String symWaterSavingCost;

    //---------------------sym_wastewater---------------------
    @Column(name = "sym_wastewater_reduction", columnDefinition = "NVARCHAR(255)")
    String symWastewaterReduction;

    @Column(name = "sym_wastewater_unit", columnDefinition = "NVARCHAR(255)")
    String symWastewaterUnit = "m3/year";

    @Column(name = "sym_wastewater_saving_cost", columnDefinition = "NVARCHAR(255)")
    String symWastewaterSavingCost;


    //---------------------sym_waste---------------------
    @Column(name = "sym_waste_treated_reduction", columnDefinition = "NVARCHAR(255)")
    String symWasteTreatedReduction;

    @Column(name = "sym_waste_reuse_amount", columnDefinition = "NVARCHAR(255)")
    String symWasteReuseAmount;

    @Column(name = "sym_waste_reuse_cost_saving", columnDefinition = "NVARCHAR(255)")
    String symWasteReuseCostSaving;

    @Column(name = "sym_waste_reuse_unit", columnDefinition = "NVARCHAR(255)")
    String symWasteReuseUnit = "ton/year";

    @Column(name = "sym_waste_recycle_amount", columnDefinition = "NVARCHAR(255)")
    String symWasteRecycleAmount;

    @Column(name = "sym_waste_recycle_cost_saving", columnDefinition = "NVARCHAR(255)")
    String symWasteRecycleCostSaving;

    @Column(name = "sym_waste_recycle_unit", columnDefinition = "NVARCHAR(255)")
    String symWasteRecycleUnit = "ton/year";



    //---------------------sym_st_material---------------------
    @Column(name = "sym_st_material_saving_quantity", columnDefinition = "NVARCHAR(255)")
    String symStMaterialSavingQuantity;

    @Column(name = "sym_st_material_cost_saving", columnDefinition = "NVARCHAR(255)")
    String symStMaterialCostSaving;

    @Column(name = "sym_st_material_unit", columnDefinition = "NVARCHAR(255)")
    String symStMaterialUnit = "ton/year";


    //---------------------sym_nd_material---------------------
    @Column(name = "sym_nd_material_saving_quantity", columnDefinition = "NVARCHAR(255)")
    String symNdMaterialSavingQuantity;

    @Column(name = "sym_nd_material_cost_saving", columnDefinition = "NVARCHAR(255)")
    String symNdMaterialCostSaving;

    @Column(name = "sym_nd_material_unit", columnDefinition = "NVARCHAR(255)")
    String symNdMaterialUnit = "ton/year";

    @Column(name = "sym_nd_material_supply_amount", columnDefinition = "NVARCHAR(255)")
    String symNdMaterialSupplyAmount;

    @Column(name = "sym_nd_material_supply_amount_unit", columnDefinition = "NVARCHAR(255)")
    String symNdMaterialSupplyAmountUnit;

    @Column(name = "sym_nd_material_supply_saving_cost", columnDefinition = "NVARCHAR(255)")
    String symNdMaterialSupplySavingCost;


    //---------------------sym_chemical---------------------
    @Column(name = "sym_chemical_saving_amount", columnDefinition = "NVARCHAR(255)")
    String symChemicalSavingAmount;

    @Column(name = "sym_chemical_cost_saving", columnDefinition = "NVARCHAR(255)")
    String symChemicalCostSaving;

    @Column(name = "sym_chemical_unit", columnDefinition = "NVARCHAR(255)")
    String symChemicalUnit = "ton/year";


    //---------------------sym_service---------------------
    @Column(name = "sym_service_sharing_list", columnDefinition = "NVARCHAR(255)")
    String symServiceSharingList;

    @Column(name = "sym_service_sharing_cost_saving", columnDefinition = "NVARCHAR(255)")
    String symServiceSharingCostSaving;
}
