package com.EIPplatform.mapper.report.report06.part03;

import com.EIPplatform.model.dto.report.report06.part03.result.ResultCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.result.ResultDTO;
import com.EIPplatform.model.entity.report.report06.part03.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResultMapper {

    ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);

    @Mapping(target = "resultId", ignore = true)
    Result toEntity(ResultCreateDTO request);

    ResultDTO toDTO(Result result);

    List<ResultDTO> toDTOList(List<Result> results);

    @Mapping(target = "resultId", ignore = true)
    void updateEntityFromDTO(ResultCreateDTO request, @MappingTarget Result entity);
}