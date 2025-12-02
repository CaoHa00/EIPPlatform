package com.EIPplatform.mapper.report.report06.part03;

import com.EIPplatform.model.dto.report.report06.part03.result.ResultCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultUpdateDTO;
import com.EIPplatform.model.entity.report.report06.part03.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface ResultMapper {
    ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);

    @Mapping(target = "inventoryResultData", ignore = true)
    @Mapping(target = "intensityRatioResult", ignore = true)
    Result toEntity(ResultCreateDTO dto);

    ResultDTO toDTO(Result entity);

    @Mapping(target = "inventoryResultData", ignore = true)
    @Mapping(target = "intensityRatioResult", ignore = true)
    void updateFromDTO(ResultUpdateDTO dto, @MappingTarget Result entity);
}