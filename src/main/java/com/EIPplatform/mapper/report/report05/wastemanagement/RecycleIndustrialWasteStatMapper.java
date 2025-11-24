package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatUpdateDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.RecycleIndustrialWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecycleIndustrialWasteStatMapper {

    RecycleIndustrialWasteStatMapper INSTANCE = Mappers.getMapper(RecycleIndustrialWasteStatMapper.class);

    @Mapping(target = "recycleId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    RecycleIndustrialWasteStat toEntity(RecycleIndustrialWasteStatCreateDTO dto);

    RecycleIndustrialWasteStatDTO toDto(RecycleIndustrialWasteStat entity);

    List<RecycleIndustrialWasteStatDTO> toDtoList(List<RecycleIndustrialWasteStat> entities);

    default void updateFromDto(RecycleIndustrialWasteStatUpdateDTO dto,
                               @MappingTarget RecycleIndustrialWasteStat entity) {

        if (dto.getTransferOrg() != null)
            entity.setTransferOrg(dto.getTransferOrg());

        if (dto.getVolumeCy() != null)
            entity.setVolumeCy(dto.getVolumeCy());

        if (dto.getUnitCy() != null)
            entity.setUnitCy(dto.getUnitCy());

        if (dto.getWasteTypeDesc() != null)
            entity.setWasteTypeDesc(dto.getWasteTypeDesc());

        if (dto.getVolumePy() != null)
            entity.setVolumePy(dto.getVolumePy());

        if (dto.getUnitPy() != null)
            entity.setUnitPy(dto.getUnitPy());
    }
}