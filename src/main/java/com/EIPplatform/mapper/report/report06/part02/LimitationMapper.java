package com.EIPplatform.mapper.report.report06.part02;

import com.EIPplatform.model.dto.report.report06.part2.LimitationDto;
import com.EIPplatform.model.entity.report.report06.part02.Limitation;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LimitationMapper {

    // ENTITY → DTO
    @Mapping(source = "operationalActivityData.operationalActivityDataId", target = "operationalActivityDataId")
    @Mapping(source = "limitationId", target = "id")
    LimitationDto toDto(Limitation entity);
    List<LimitationDto> toDtoList(List<Limitation> entities);

    // DTO → ENTITY
    @Mapping(target = "limitationId", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    Limitation toEntity(LimitationDto dto);

    List<Limitation> toEntityList(List<LimitationDto> dtos);

    // PATCH UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "operationalActivityData", ignore = true)
    void updateEntity(@MappingTarget Limitation entity, LimitationDto dto);
}
