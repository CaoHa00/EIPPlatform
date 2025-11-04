package com.EIPplatform.mapper.report.wastemanagement;

import com.EIPplatform.model.dto.report.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatDTO;
import com.EIPplatform.model.entity.report.wastemanagement.IndustrialSolidWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IndustrialSolidWasteStatMapper {
    IndustrialSolidWasteStatMapper INSTANCE = Mappers.getMapper(IndustrialSolidWasteStatMapper.class);

    @Mapping(target = "industrialId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    IndustrialSolidWasteStat toEntity(IndustrialSolidWasteStatCreateDTO dto);

    @Mapping(source = "industrialId", target = "industrialId")
    IndustrialSolidWasteStatDTO toDto(IndustrialSolidWasteStat entity);

    List<IndustrialSolidWasteStatDTO> toDtoList(List<IndustrialSolidWasteStat> entities);

    default void updateFromDto(IndustrialSolidWasteStatCreateDTO dto, @org.mapstruct.MappingTarget IndustrialSolidWasteStat entity) {
        entity.setWasteGroup(dto.getWasteGroup());
        entity.setVolumeCy(dto.getVolumeCy());
        entity.setReceiverOrg(dto.getReceiverOrg());
        entity.setVolumePy(dto.getVolumePy());
    }
}