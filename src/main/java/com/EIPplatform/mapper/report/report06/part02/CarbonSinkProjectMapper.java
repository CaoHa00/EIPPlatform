package com.EIPplatform.mapper.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject.CarbonSinkProjectCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject.CarbonSinkProjectDTO;
import com.EIPplatform.model.dto.report.report06.part02.carbonSinkProject.CarbonSinkProjectUpdateDTO;
import com.EIPplatform.model.entity.report.report06.part02.CarbonSinkProject;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarbonSinkProjectMapper {

    CarbonSinkProjectMapper INSTANCE = Mappers.getMapper(CarbonSinkProjectMapper.class);

    @Mapping(target = "carbonSinkProjectId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    CarbonSinkProject toEntity(CarbonSinkProjectCreateDTO createDTO);

    CarbonSinkProjectDTO toDTO(CarbonSinkProject entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "carbonSinkProjectId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    void updateEntityFromDTO(CarbonSinkProjectUpdateDTO updateDTO, @MappingTarget CarbonSinkProject entity);

    List<CarbonSinkProject> toEntityList(List<CarbonSinkProjectCreateDTO> createDTOList);

    List<CarbonSinkProjectDTO> toDTOList(List<CarbonSinkProject> entityList);
}