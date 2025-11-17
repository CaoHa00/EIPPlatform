package com.EIPplatform.model.dto.report.reportB04.part4.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SymbiosisIndustryCreateDTO {

    // Indus
    String indusSymCount;
    String indusSymType;

    // is_sym
    Boolean isSymIoExchange;
    Boolean isSymSharedInfrastructure;
    Boolean isSymTypeSharedServices;
    Boolean isSymResourceRecovery;

    // symbiosis
    String symbiosisTypeOther;
    String symbiosisDescOther;

    // sym_electricity
    String symElectricitySaving;
    @NotNull
    String symElectricityUnit;
    String symElectricityCo2Reduction;
    String symElectricitySavingCost;
    String symElectricityOtherBenefits;

    // sym_fuel
    @NotNull
    String symFuelSavingAmount;
    @NotNull
    String symFuelSavingUnit;
    @NotNull
    String symFuelCo2Reduction;
    @NotNull
    String symFuelSavingCost;
    @NotNull
    String symFuelOtherBenefits;

    // Total
    @NotNull
    String symTotalCo2Reduction;

    // sym_water
    String symWaterSaving;
    @NotNull
    String symWaterSavingUnit;
    String symWaterSavingCost;

    // sym_wastewater
    String symWastewaterReduction;
    @NotNull
    String symWastewaterUnit;
    String symWastewaterSavingCost;

    // sym_waste
    String symWasteTreatedReduction;
    String symWasteReuseAmount;
    String symWasteReuseCostSaving;
    @NotNull
    String symWasteReuseUnit;
    String symWasteRecycleAmount;
    String symWasteRecycleCostSaving;
    @NotNull
    String symWasteRecycleUnit;

    // sym_st_material
    String symStMaterialSavingQuantity;
    String symStMaterialCostSaving;
    @NotNull
    String symStMaterialUnit;

    // sym_nd_material
    String symNdMaterialSavingQuantity;
    String symNdMaterialCostSaving;
    @NotNull
    String symNdMaterialUnit;
    String symNdMaterialSupplyAmount;
    @NotNull
    String symNdMaterialSupplyAmountUnit;
    String symNdMaterialSupplySavingCost;

    // sym_chemical
    String symChemicalSavingAmount;
    String symChemicalCostSaving;
    @NotNull
    String symChemicalUnit;

    // sym_service
    String symServiceSharingList;
    String symServiceSharingCostSaving;

    // Relationships (optional - include only if needed)
    private UUID reportB04Id;
    private List<SymCompanyCreateDTO> symCompanies;
}