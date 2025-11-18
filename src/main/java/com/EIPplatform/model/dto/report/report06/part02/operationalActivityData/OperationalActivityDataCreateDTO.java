package com.EIPplatform.model.dto.report.report06.part02.operationalActivityData;

import com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject.CarbonSinkProjectCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.emissionSource.EmissionSourceCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.limitation.LimitationCreateDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationalActivityDataCreateDTO {
    UUID scaleCapacityId; // Optional ManyToOne to ScaleCapacity

    List<UUID> facilityIds; // Optional ManyToMany: IDs of Facilities

    List<UUID> processIds; // Optional ManyToMany: IDs of Processes

    List<UUID> equipmentIds; // Optional ManyToMany: IDs of Equipments

    @Valid
    @NotNull(message = "IS_REQUIRED")
    List<@Valid EmissionSourceCreateDTO> emissionSources;

    @Valid
    List<@Valid CarbonSinkProjectCreateDTO> carbonSinkProjects;

    @Valid
    List<@Valid LimitationCreateDTO> limitations;

    @NotBlank(message = "IS_REQUIRED")
    String dataManagementProcedure;

    @NotBlank(message = "IS_REQUIRED")
    String emissionFactorSource;
}