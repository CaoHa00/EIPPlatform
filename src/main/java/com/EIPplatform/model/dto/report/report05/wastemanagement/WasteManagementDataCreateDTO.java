package com.EIPplatform.model.dto.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteManagementDataCreateDTO {

    String swGeneralNote;

    @Valid
    List<@Valid DomesticSolidWasteStatCreateDTO> domesticSolidWasteStats;

    @Valid
    List<@Valid IndustrialSolidWasteStatCreateDTO> industrialSolidWasteStats;

    @Valid
    List<@Valid RecycleIndustrialWasteStatCreateDTO> recycleIndustrialWasteStats;

    @Valid
    List<@Valid OtherSolidWasteStatCreateDTO> otherSolidWasteStats;

    @Valid
    List<@Valid HazardousWasteStatCreateDTO> hazardousWasteStats;

    @Valid
    List<@Valid ExportedHwStatCreateDTO> exportedHwStats;

    @Valid
    List<@Valid SelfTreatedHwStatCreateDTO> selfTreatedHwStats;

    @NotBlank(message = "IS_REQUIRED")
    String incidentPlanDevelopment;

    @NotBlank(message = "IS_REQUIRED")
    String incidentPreventionMeasures;

    @NotBlank(message = "IS_REQUIRED")
    String incidentResponseReport;

    @Valid
    List<@Valid PopInventoryStatCreateDTO> popInventoryStats;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double waterTotalVolume;

    @NotBlank(message = "IS_REQUIRED")
    String waterTotalUnit;

    @NotBlank(message = "IS_REQUIRED")
    String waterEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double airTotalVolume;

    @NotBlank(message = "IS_REQUIRED")
    String airTotalUnit;

    @NotBlank(message = "IS_REQUIRED")
    String airEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double soilTotalVolume;

    @NotBlank(message = "IS_REQUIRED")
    String soilTotalUnit;

    @NotBlank(message = "IS_REQUIRED")
    String soilEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double sewageSludgeTotalVolume;

    @NotBlank(message = "IS_REQUIRED")
    String sewageSludgeTotalUnit;

    @NotBlank(message = "IS_REQUIRED")
    String sewageSludgeEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double hwOnsiteTotalVolume;

    @NotBlank(message = "IS_REQUIRED")
    String hwOnsiteTotalUnit;

    @NotBlank(message = "IS_REQUIRED")
    String hwOnsiteEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double hwRecycleTotalVolume;

    @NotBlank(message = "IS_REQUIRED")
    String hwRecycleTotalUnit;

    @NotBlank(message = "IS_REQUIRED")
    String hwRecycleEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    Double hwDisposalTotalVolume;

    @NotBlank(message = "IS_REQUIRED")
    String hwDisposalTotalUnit;

    @NotBlank(message = "IS_REQUIRED")
    String hwDisposalEstimationMethod;
}