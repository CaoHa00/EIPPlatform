package com.EIPplatform.mapper.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.CarbonSinkProjectDto;
import com.EIPplatform.model.entity.report.report06.part02.CarbonSinkProject;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarbonSinkProjectMapper {

    // ENTITY → DTO
    @Mapping(source = "operationalActivityData.operationalActivityDataId", target = "operationalActivityData")
    CarbonSinkProjectDto toDto(CarbonSinkProject entity);
    List<CarbonSinkProjectDto> toDtoList(List<CarbonSinkProject> entities);

    // DTO → ENTITY
    @Mapping(target = "carbonSinkProjectId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true) // set in service
    CarbonSinkProject toEntity(CarbonSinkProjectDto dto);

    List<CarbonSinkProject> toEntityList(List<CarbonSinkProjectDto> dtos);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "operationalActivityData", ignore = true)
    void updateEntity(@MappingTarget CarbonSinkProject entity, CarbonSinkProjectDto dto);
}
