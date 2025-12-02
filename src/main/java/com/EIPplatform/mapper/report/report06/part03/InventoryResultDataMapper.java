package com.EIPplatform.mapper.report.report06.part03;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataDTO;
import com.EIPplatform.model.entity.report.report06.part03.InventoryResultData;

@Mapper(componentModel = "spring", uses = {EmissionDataMapper.class, ResultMapper.class, UncertaintyEvaluationMapper.class})
public interface InventoryResultDataMapper {
    InventoryResultDataMapper INSTANCE = Mappers.getMapper(InventoryResultDataMapper.class);

    @Mapping(target = "inventoryResultDataId", ignore = true)
    @Mapping(target = "report06", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    @Mapping(target = "emissionDatas", source = "emissionDatas")
    @Mapping(target = "resultEntity", source = "resultEntity")
    @Mapping(target = "uncertaintyEvaluations", source = "uncertaintyEvaluations")
    @Mapping(target = "result", ignore = true) // Auto-generated, ignore
    InventoryResultData toEntity(InventoryResultDataCreateDTO dto);

    @Mapping(source = "resultEntity", target = "resultEntity")
    @Mapping(source = "emissionDatas", target = "emissionDatas")
    @Mapping(source = "uncertaintyEvaluations", target = "uncertaintyEvaluations")
    InventoryResultDataDTO toDTO(InventoryResultData entity);

    // InventoryResultDataMapper
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "inventoryResultDataId", ignore = true)
    @Mapping(target = "report06", ignore = true)
    @Mapping(target = "operationalActivityData", ignore = true)
    @Mapping(target = "emissionDatas", ignore = true)
    @Mapping(target = "resultEntity", ignore = true)
    @Mapping(target = "uncertaintyEvaluations", ignore = true)
    @Mapping(target = "result", ignore = true)
    void updateEntityFromDto(InventoryResultDataDTO dto,
                             @MappingTarget InventoryResultData entity);
}