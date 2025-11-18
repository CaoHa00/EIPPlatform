package com.EIPplatform.model.dto.report.reportB04.part3;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourcesSavingAndReductionDTO {
    Long rsarId;
    String appliedEnergySavingModels;

    // Electricity
    String electricitySaving;
    String electricityUnit;
    String electricityCo2Reduction;
    String electricitySavingCost;
    String electricityOtherBenefits;

    // Fuel
    String fuelSavingAmount;
    String fuelSavingUnit;
    String fuelCo2Reduction;
    String fuelSavingCost;
    String fuelOtherBenefits;

    // Total CO2 Reduction
    String totalCo2Reduction;

    // Water
    String waterSaving;
    String waterSavingUnit;
    String waterSavingCost;

    // Wastewater
    String wastewaterReduction;
    String wastewaterUnit;
    String wastewaterSavingCost;

    // Waste
    String wasteTreatedReduction;
    String wasteReuseAmount;
    String wasteReuseCostSaving;
    String wasteReuseUnit;
    String wasteRecycleAmount;
    String wasteRecycleCostSaving;
    String wasteRecycleUnit;

    // Primary Material
    String primaryMaterialSavingQuantity;
    String primaryMaterialCostSaving;
    String primaryMaterialUnit;

    // Chemical
    String chemicalSavingAmount;
    String chemicalCostSaving;
    String chemicalUnit;


}
