package com.EIPplatform.model.dto.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatDTO;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteManagementDataDTO {
    Long wmId;
    String swGeneralNote;

    @Valid
    List<@Valid DomesticSolidWasteStatDTO> domesticSolidWasteStats;

    @Valid
    List<@Valid IndustrialSolidWasteStatDTO> industrialSolidWasteStats;

    @Valid
    List<@Valid RecycleIndustrialWasteStatDTO> recycleIndustrialWasteStats;

    @Valid
    List<@Valid OtherSolidWasteStatDTO> otherSolidWasteStats;

    @Valid
    List<@Valid HazardousWasteStatDTO> hazardousWasteStats;

    @Valid
    List<@Valid ExportedHwStatDTO> exportedHwStats;

    @Valid
    List<@Valid SelfTreatedHwStatDTO> selfTreatedHwStats;

    String incidentPlanDevelopment;
    String incidentPreventionMeasures;
    String incidentResponseReport;

    @Valid
    List<@Valid PopInventoryStatDTO> popInventoryStats;

    BigDecimal waterTotalVolumeKg;
    String waterEstimationMethod;
    BigDecimal airTotalVolumeKg;
    String airEstimationMethod;
    BigDecimal soilTotalVolumeKg;
    String soilEstimationMethod;
    BigDecimal sewageSludgeTotalVolumeKg;
    String sewageSludgeEstimationMethod;
    BigDecimal hwOnsiteTotalVolumeKg;
    String hwOnsiteEstimationMethod;
    BigDecimal hwRecycleTotalVolumeKg;
    String hwRecycleEstimationMethod;
    BigDecimal hwDisposalTotalVolumeKg;
    String hwDisposalEstimationMethod;

    LocalDateTime createdAt;
}