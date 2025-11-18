package com.EIPplatform.mapper.report.report06.part03;

import com.EIPplatform.model.dto.report.report06.part03.emissionData.EmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.emissionData.EmissionDataDTO;
import com.EIPplatform.model.dto.report.report06.part03.emissionData.EmissionDataUpdateDTO;
import com.EIPplatform.model.entity.report.report06.part03.EmissionData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {MonthlyEmissionDataMapper.class})
public interface EmissionDataMapper {

    @Mapping(target = "inventoryResultData", ignore = true)
    @Mapping(target = "emissionSource", ignore = true)
    @Mapping(target = "totalAnnualData", ignore = true)
    @Mapping(target = "monthlyDatas", source = "monthlyDatas")
    EmissionData toEntity(EmissionDataCreateDTO dto);

    @Mapping(source = "monthlyDatas", target = "monthlyDatas")
    @Mapping(source = "emissionSource.sourceCode", target = "sourceCode")
    @Mapping(source = "emissionSource.sourceName", target = "sourceName")
    EmissionDataDTO toDTO(EmissionData entity);

    @Mapping(target = "inventoryResultData", ignore = true)
    @Mapping(target = "emissionSource", ignore = true)
    @Mapping(target = "totalAnnualData", ignore = true)
    @Mapping(target = "monthlyDatas", source = "monthlyDatas")
    void updateFromDTO(EmissionDataUpdateDTO dto, @MappingTarget EmissionData entity);
}
