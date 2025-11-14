package com.EIPplatform.mapper.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.OperationalActivityDataDto;
import com.EIPplatform.model.entity.report.report06.part02.OperationalActivityData;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OperationalActivityDataMapper {

    // ENTITY → DTO
    @Mapping(source = "operationalActivityDataId", target = "operationalActivityDataId")
    @Mapping(source = "report06.report06Id", target = "reportId")
    @Mapping(target = "activityName", ignore = true)
    // DTO field not in entity
    OperationalActivityDataDto toDto(OperationalActivityData entity);

    List<OperationalActivityDataDto> toDtoList(List<OperationalActivityData> entities);

    // DTO → ENTITY
    @Mapping(target = "operationalActivityDataId", ignore = true)
    @Mapping(target = "report06", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "emissionSources", ignore = true)
    @Mapping(target = "carbonSinkProjects", ignore = true)
    @Mapping(target = "limitations", ignore = true)
    @Mapping(target = "dataManagementProcedure", ignore = true)
    @Mapping(target = "emissionFactorSource", ignore = true)
    OperationalActivityData toEntity(OperationalActivityDataDto dto);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "report06", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "emissionSources", ignore = true)
    @Mapping(target = "carbonSinkProjects", ignore = true)
    @Mapping(target = "limitations", ignore = true)
    @Mapping(target = "dataManagementProcedure", ignore = true)
    @Mapping(target = "emissionFactorSource", ignore = true)
    void updateEntity(@MappingTarget OperationalActivityData entity, OperationalActivityDataDto dto);
}
