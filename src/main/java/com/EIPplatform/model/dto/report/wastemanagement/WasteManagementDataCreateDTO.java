package com.EIPplatform.model.dto.report.wastemanagement;

import com.EIPplatform.model.dto.report.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.exportedhwstat.ExportedHwStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.hazardouswastestat.HazardousWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.othersolidwastestat.OtherSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.popinventorystat.PopInventoryStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.selftreatedhwstat.SelfTreatedHwStatCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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
    BigDecimal waterTotalVolumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String waterEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal airTotalVolumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String airEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal soilTotalVolumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String soilEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal sewageSludgeTotalVolumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String sewageSludgeEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal hwOnsiteTotalVolumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String hwOnsiteEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal hwRecycleTotalVolumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String hwRecycleEstimationMethod;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal hwDisposalTotalVolumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String hwDisposalEstimationMethod;
}