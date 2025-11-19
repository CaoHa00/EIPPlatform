package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.project.ProjectResponseDto;
import com.EIPplatform.model.entity.businessInformation.Project;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    // ENTITY → RESPONSE
    @Mapping(source = "businessDetail.businessDetailId", target = "businessDetailId")
    ProjectResponseDto toResponse(Project entity);

    List<ProjectResponseDto> toResponseList(List<Project> entities);

    // DTO → ENTITY
    @Mapping(target = "businessDetail", ignore = true)
    Project toEntity(ProjectResponseDto dto);

    List<Project> toEntityList(List<ProjectResponseDto> dtos);

    // PARTIAL UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "businessDetail", ignore = true)
    void updateEntity(@MappingTarget Project entity, ProjectResponseDto dto);
}
