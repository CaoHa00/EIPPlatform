
package com.EIPplatform.model.dto.report.reportB04.part3.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourcesSavingAndReductionUpdateRequestDTO {

    //Foreign key
    UUID reportId;

    @NotNull
    String appliedEnergySavingModels;

    // Electricity
    @NotNull
    String electricitySaving;
    @NotNull
    String electricityUnit;
    @NotNull
    String electricityCo2Reduction;
    @NotNull
    String electricitySavingCost;
    @NotNull
    String electricityOtherBenefits;

    // Fuel
    @NotNull
    String fuelSavingAmount;
    @NotNull
    String fuelSavingUnit;
    @NotNull
    String fuelCo2Reduction;
    @NotNull
    String fuelSavingCost;
    @NotNull
    String fuelOtherBenefits;

    // Total CO2 Reduction
    @NotNull
    String totalCo2Reduction;

    // Water
    @NotNull
    String waterSaving;
    @NotNull
    String waterSavingUnit;
    @NotNull
    String waterSavingCost;

    // Wastewater
    @NotNull
    String wastewaterReduction;
    @NotNull
    String wastewaterUnit;
    @NotNull
    String wastewaterSavingCost;

    // Waste
    @NotNull
    String wasteTreatedReduction;
    @NotNull
    String wasteReuseAmount;
    @NotNull
    String wasteReuseCostSaving;
    @NotNull
    String wasteReuseUnit;
    @NotNull
    String wasteRecycleAmount;
    @NotNull
    String wasteRecycleCostSaving;
    @NotNull
    String wasteRecycleUnit;

    // Primary Material
    @NotNull
    String primaryMaterialSavingQuantity;
    @NotNull
    String primaryMaterialCostSaving;
    @NotNull
    String primaryMaterialUnit;

    // Chemical
    @NotNull
    String chemicalSavingAmount;
    @NotNull
    String chemicalCostSaving;
    @NotNull
    String chemicalUnit;
}