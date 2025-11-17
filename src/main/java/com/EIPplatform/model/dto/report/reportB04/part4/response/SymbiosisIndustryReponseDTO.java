package com.EIPplatform.model.dto.report.reportB04.part4.response;

import com.EIPplatform.model.dto.report.reportB04.part4.request.SymCompanyCreateDTO;
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
public class SymbiosisIndustryReponseDTO {

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
    String symElectricityUnit;
    String symElectricityCo2Reduction;
    String symElectricitySavingCost;
    String symElectricityOtherBenefits;

    // sym_fuel
    String symFuelSavingAmount;
    String symFuelSavingUnit;
    String symFuelCo2Reduction;
    String symFuelSavingCost;
    String symFuelOtherBenefits;

    // Total
    String symTotalCo2Reduction;

    // sym_water
    String symWaterSaving;
    String symWaterSavingUnit;
    String symWaterSavingCost;

    // sym_wastewater
    String symWastewaterReduction;
    String symWastewaterUnit;
    String symWastewaterSavingCost;

    // sym_waste
    String symWasteTreatedReduction;
    String symWasteReuseAmount;
    String symWasteReuseCostSaving;
    String symWasteReuseUnit;
    String symWasteRecycleAmount;
    String symWasteRecycleCostSaving;
    String symWasteRecycleUnit;

    // sym_st_material
    String symStMaterialSavingQuantity;
    String symStMaterialCostSaving;
    String symStMaterialUnit;

    // sym_nd_material
    String symNdMaterialSavingQuantity;
    String symNdMaterialCostSaving;
    String symNdMaterialUnit;
    String symNdMaterialSupplyAmount;
    String symNdMaterialSupplyAmountUnit;
    String symNdMaterialSupplySavingCost;

    // sym_chemical
    String symChemicalSavingAmount;
    String symChemicalCostSaving;
    String symChemicalUnit;

    // sym_service
    String symServiceSharingList;
    String symServiceSharingCostSaving;

    // Relationships (optional - include only if needed)
    private UUID reportB04Id;
    private List<SymCompanyResponseDTO> symCompanies;
}