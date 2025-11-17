package com.EIPplatform.mapper.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.EmissionSourceDto;
import com.EIPplatform.model.entity.report.report06.part02.EmissionSource;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmissionSourceMapper {

    // ENTITY → DTO
    @Mapping(source = "operationalActivityData.operationalActivityDataId", target = "operationalActivityDataId")
    @Mapping(target = "sourceScope", expression = "java(String.valueOf(entity.getSourceScope()))")
    EmissionSourceDto toDto(EmissionSource entity);
    List<EmissionSourceDto> toDtoList(List<EmissionSource> entities);

    // DTO → ENTITY
    @Mapping(target = "emissionSourceId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    @Mapping(target = "sourceScope", expression = "java(Integer.valueOf(dto.getSourceScope()))")
    EmissionSource toEntity(EmissionSourceDto dto);

    List<EmissionSource> toEntityList(List<EmissionSourceDto> dtos);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "operationalActivityData", ignore = true)
    @Mapping(target = "sourceScope", expression = "java(dto.getSourceScope() != null ? Integer.valueOf(dto.getSourceScope()) : entity.getSourceScope())")
    void updateEntity(@MappingTarget EmissionSource entity, EmissionSourceDto dto);
}
