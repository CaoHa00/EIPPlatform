package com.EIPplatform.mapper.report.report06.part03;

import com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData.MonthlyEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData.MonthlyEmissionDataUpdateDTO;
import com.EIPplatform.model.entity.report.report06.part03.MonthlyEmissionData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MonthlyEmissionDataMapper {
    MonthlyEmissionDataMapper INSTANCE = Mappers.getMapper(MonthlyEmissionDataMapper.class);

    MonthlyEmissionData toEntity(MonthlyEmissionDataCreateDTO dto);

    MonthlyEmissionData toDTO(MonthlyEmissionData entity);

    @Mapping(target = "id", ignore = true) // Ignore ID for update
    void updateFromDTO(MonthlyEmissionDataUpdateDTO dto, @MappingTarget MonthlyEmissionData entity);
}