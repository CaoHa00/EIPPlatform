package com.EIPplatform.mapper.report.report06.part02;
import com.EIPplatform.model.dto.report.report06.part02.emissionSource.EmissionSourceCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.emissionSource.EmissionSourceDTO;
import com.EIPplatform.model.dto.report.report06.part02.emissionSource.EmissionSourceUpdateDTO;
import com.EIPplatform.model.entity.report.report06.part02.EmissionSource;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmissionSourceMapper {

    EmissionSourceMapper INSTANCE = Mappers.getMapper(EmissionSourceMapper.class);

    @Mapping(target = "emissionSourceId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    @Mapping(target = "ghgEmitted", ignore = true)
    EmissionSource toEntity(EmissionSourceCreateDTO createDTO);

    EmissionSourceDTO toDTO(EmissionSource entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "emissionSourceId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    @Mapping(target = "ghgEmitted", ignore = true)
    void updateEntityFromDTO(EmissionSourceUpdateDTO updateDTO, @MappingTarget EmissionSource entity);

    List<EmissionSource> toEntityList(List<EmissionSourceCreateDTO> createDTOList);

    List<EmissionSourceDTO> toDTOList(List<EmissionSource> entityList);
}