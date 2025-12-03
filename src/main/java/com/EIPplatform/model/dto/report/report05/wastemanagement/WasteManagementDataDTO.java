package com.EIPplatform.model.dto.report.report05.wastemanagement;

import java.time.LocalDateTime;
import java.util.List;

import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatDTO;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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
    @Valid
    List<@Valid PopInventoryStatDTO> popInventoryStats;

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

    LocalDateTime createdAt;
}