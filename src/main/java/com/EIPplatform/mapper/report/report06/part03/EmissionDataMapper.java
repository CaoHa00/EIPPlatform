package com.EIPplatform.mapper.report.report06.part03;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmissionDataMapper {
    EmissionDataMapper INSTANCE = Mappers.getMapper(EmissionDataMapper.class);

    
}
