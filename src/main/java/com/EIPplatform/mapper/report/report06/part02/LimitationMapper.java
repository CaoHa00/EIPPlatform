package com.EIPplatform.mapper.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part02.limitation.LimitationCreateDTO;
import com.EIPplatform.model.dto.report.report06.part02.limitation.LimitationDTO;
import com.EIPplatform.model.dto.report.report06.part02.limitation.LimitationUpdateDTO;
import com.EIPplatform.model.entity.report.report06.part02.Limitation;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface LimitationMapper {

    LimitationMapper INSTANCE = Mappers.getMapper(LimitationMapper.class);

    @Mapping(target = "limitationId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    Limitation toEntity(LimitationCreateDTO createDTO);

    LimitationDTO toDTO(Limitation entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "limitationId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    void updateEntityFromDTO(LimitationUpdateDTO updateDTO, @MappingTarget Limitation entity);

    List<Limitation> toEntityList(List<LimitationCreateDTO> createDTOList);

    List<LimitationDTO> toDTOList(List<Limitation> entityList);
}
