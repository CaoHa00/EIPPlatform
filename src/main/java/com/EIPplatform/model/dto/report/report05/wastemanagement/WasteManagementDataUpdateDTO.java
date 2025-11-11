package com.EIPplatform.model.dto.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatUpdateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatUpdateDTO;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteManagementDataUpdateDTO {

    String swGeneralNote;

    @Valid
    List<@Valid DomesticSolidWasteStatUpdateDTO> domesticSolidWasteStats;

    @Valid
    List<@Valid IndustrialSolidWasteStatUpdateDTO> industrialSolidWasteStats;

    @Valid
    List<@Valid RecycleIndustrialWasteStatUpdateDTO> recycleIndustrialWasteStats;

    @Valid
    List<@Valid OtherSolidWasteStatUpdateDTO> otherSolidWasteStats;

    @Valid
    List<@Valid HazardousWasteStatUpdateDTO> hazardousWasteStats;

    @Valid
    List<@Valid ExportedHwStatUpdateDTO> exportedHwStats;

    @Valid
    List<@Valid SelfTreatedHwStatUpdateDTO> selfTreatedHwStats;

    String incidentPlanDevelopment;

    String incidentPreventionMeasures;

    String incidentResponseReport;

    @Valid
    List<@Valid PopInventoryStatUpdateDTO> popInventoryStats;

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
}