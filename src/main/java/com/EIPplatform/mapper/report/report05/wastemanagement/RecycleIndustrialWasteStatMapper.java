package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat.RecycleIndustrialWasteStatDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.RecycleIndustrialWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecycleIndustrialWasteStatMapper {
    RecycleIndustrialWasteStatMapper INSTANCE = Mappers.getMapper(RecycleIndustrialWasteStatMapper.class);

    @Mapping(target = "recycleId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    RecycleIndustrialWasteStat toEntity(RecycleIndustrialWasteStatCreateDTO dto);

    @Mapping(source = "recycleId", target = "recycleId")
    RecycleIndustrialWasteStatDTO toDto(RecycleIndustrialWasteStat entity);

    List<RecycleIndustrialWasteStatDTO> toDtoList(List<RecycleIndustrialWasteStat> entities);

    default void updateFromDto(RecycleIndustrialWasteStatCreateDTO dto, @org.mapstruct.MappingTarget RecycleIndustrialWasteStat entity) {
        entity.setTransferOrg(dto.getTransferOrg());
        entity.setVolumeCy(dto.getVolumeCy());
        entity.setWasteTypeDesc(dto.getWasteTypeDesc());
        entity.setVolumePy(dto.getVolumePy());
    }
}