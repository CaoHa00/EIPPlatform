package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.project.ProjectCreateRequest;
import com.EIPplatform.model.dto.businessInformation.project.ProjectDTO;
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

    Project toEntityFromCreate(ProjectCreateRequest dto);

    @Named("toProjectDTO")
    default ProjectDTO toDTO(Project entity) {
        if (entity == null)
            return null;
        return ProjectDTO.builder()
                .projectId(entity.getProjectId())
                .projectName(entity.getProjectName())
                .projectLocation(entity.getProjectLocation())
                .projectLegalDocType(entity.getProjectLegalDocType())
                .projectIssuerOrg(entity.getProjectIssuerOrg())
                .projectIssueDate(entity.getProjectIssueDate())
                .projectIssueDateLatest(entity.getProjectIssueDateLatest())
                .businessDetailId(
                        entity.getBusinessDetail() != null ? entity.getBusinessDetail().getBusinessDetailId() : null)
                .build();
    }

}
