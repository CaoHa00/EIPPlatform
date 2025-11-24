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

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WasteManagementDataUpdateDTO {

    String swGeneralNote;

    @Valid List<@Valid DomesticSolidWasteStatUpdateDTO> domesticSolidWasteStats;
    @Valid List<@Valid IndustrialSolidWasteStatUpdateDTO> industrialSolidWasteStats;
    @Valid List<@Valid RecycleIndustrialWasteStatUpdateDTO> recycleIndustrialWasteStats;
    @Valid List<@Valid OtherSolidWasteStatUpdateDTO> otherSolidWasteStats;
    @Valid List<@Valid HazardousWasteStatUpdateDTO> hazardousWasteStats;
    @Valid List<@Valid ExportedHwStatUpdateDTO> exportedHwStats;
    @Valid List<@Valid SelfTreatedHwStatUpdateDTO> selfTreatedHwStats;
    @Valid List<@Valid PopInventoryStatUpdateDTO> popInventoryStats;

    String incidentPlanDevelopment;
    String incidentPreventionMeasures;
    String incidentResponseReport;

    Double waterTotalVolume;
    String waterTotalUnit;
    String waterEstimationMethod;

    Double airTotalVolume;
    String airTotalUnit;
    String airEstimationMethod;

    Double soilTotalVolume;
    String soilTotalUnit;
    String soilEstimationMethod;

    Double sewageSludgeTotalVolume;
    String sewageSludgeTotalUnit;
    String sewageSludgeEstimationMethod;

    Double hwOnsiteTotalVolume;
    String hwOnsiteTotalUnit;
    String hwOnsiteEstimationMethod;

    Double hwRecycleTotalVolume;
    String hwRecycleTotalUnit;
    String hwRecycleEstimationMethod;

    Double hwDisposalTotalVolume;
    String hwDisposalTotalUnit;
    String hwDisposalEstimationMethod;
}