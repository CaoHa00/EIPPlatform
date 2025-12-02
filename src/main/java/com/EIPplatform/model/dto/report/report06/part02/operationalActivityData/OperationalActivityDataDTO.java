package com.EIPplatform.model.dto.report.report06.part02.operationalActivityData;

import com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject.CarbonSinkProjectDTO;
import com.EIPplatform.model.dto.report.report06.part02.emissionSource.EmissionSourceDTO;
import com.EIPplatform.model.dto.report.report06.part02.limitation.LimitationDTO;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationalActivityDataDTO {
    UUID operationalActivityDataId;

    UUID scaleCapacityId;
    List<UUID> facilityIds;
    List<UUID> processIds;
    List<UUID> equipmentIds;

    @Valid
    List<@Valid EmissionSourceDTO> emissionSources;

    @Valid
    List<@Valid CarbonSinkProjectDTO> carbonSinkProjects;

    @Valid
    List<@Valid LimitationDTO> limitations;

    String dataManagementProcedure;
    String emissionFactorSource;
}