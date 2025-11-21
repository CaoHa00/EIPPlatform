package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat.IndustrialSolidWasteStatUpdateDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.IndustrialSolidWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IndustrialSolidWasteStatMapper {
    IndustrialSolidWasteStatMapper INSTANCE = Mappers.getMapper(IndustrialSolidWasteStatMapper.class);

    @Mapping(target = "industrialId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    IndustrialSolidWasteStat toEntity(IndustrialSolidWasteStatCreateDTO dto);

    IndustrialSolidWasteStatDTO toDto(IndustrialSolidWasteStat entity);

    List<IndustrialSolidWasteStatDTO> toDtoList(List<IndustrialSolidWasteStat> entities);

    default void updateFromDto(
            IndustrialSolidWasteStatUpdateDTO dto,
            @MappingTarget IndustrialSolidWasteStat entity
    ) {
        if (dto.getWasteGroup() != null)
            entity.setWasteGroup(dto.getWasteGroup());

        if (dto.getVolumeCy() != null)
            entity.setVolumeCy(dto.getVolumeCy());

        if (dto.getUnitCy() != null)
            entity.setUnitCy(dto.getUnitCy());

        if (dto.getReceiverOrg() != null)
            entity.setReceiverOrg(dto.getReceiverOrg());

        if (dto.getVolumePy() != null)
            entity.setVolumePy(dto.getVolumePy());

        if (dto.getUnitPy() != null)
            entity.setUnitPy(dto.getUnitPy());
    }

}