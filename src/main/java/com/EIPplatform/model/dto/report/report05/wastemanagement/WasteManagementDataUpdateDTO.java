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

import java.lang.Double;
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

    Double waterTotalVolumeKg;

    String waterEstimationMethod;

    Double airTotalVolumeKg;

    String airEstimationMethod;

    Double soilTotalVolumeKg;

    String soilEstimationMethod;

    Double sewageSludgeTotalVolumeKg;

    String sewageSludgeEstimationMethod;

    Double hwOnsiteTotalVolumeKg;

    String hwOnsiteEstimationMethod;

    Double hwRecycleTotalVolumeKg;

    String hwRecycleEstimationMethod;

    Double hwDisposalTotalVolumeKg;

    String hwDisposalEstimationMethod;
}