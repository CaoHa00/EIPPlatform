package com.EIPplatform.model.dto.report.report06.part02.operationalActivityData;

import com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject.CarbonSinkProjectUpdateDTO;
import com.EIPplatform.model.dto.report.report06.part02.emissionSource.EmissionSourceUpdateDTO;
import com.EIPplatform.model.dto.report.report06.part02.limitation.LimitationUpdateDTO;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationalActivityDataUpdateDTO {
    UUID scaleCapacityId;

    List<UUID> facilityIds;

    List<UUID> processIds;

    List<UUID> equipmentIds;

    @Valid
    List<@Valid EmissionSourceUpdateDTO> emissionSources;

    @Valid
    List<@Valid CarbonSinkProjectUpdateDTO> carbonSinkProjects;

    @Valid
    List<@Valid LimitationUpdateDTO> limitations;

    String dataManagementProcedure;

    String emissionFactorSource;
}