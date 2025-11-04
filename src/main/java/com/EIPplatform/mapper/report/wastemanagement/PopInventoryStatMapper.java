package com.EIPplatform.mapper.report.wastemanagement;

import com.EIPplatform.model.dto.report.wastemanagement.popinventorystat.PopInventoryStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.popinventorystat.PopInventoryStatDTO;
import com.EIPplatform.model.entity.report.wastemanagement.PopInventoryStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PopInventoryStatMapper {
    PopInventoryStatMapper INSTANCE = Mappers.getMapper(PopInventoryStatMapper.class);

    @Mapping(target = "popInventoryId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    @Mapping(target = "casCode", ignore = true) // Optional
    @Mapping(target = "importDate", ignore = true) // Optional
    @Mapping(target = "concentration", ignore = true) // Optional
    @Mapping(target = "complianceResult", ignore = true) // Optional
    PopInventoryStat toEntity(PopInventoryStatCreateDTO dto);

    @Mapping(source = "popInventoryId", target = "popInventoryId")
    PopInventoryStatDTO toDto(PopInventoryStat entity);

    List<PopInventoryStatDTO> toDtoList(List<PopInventoryStat> entities);

    default void updateFromDto(PopInventoryStatCreateDTO dto, @org.mapstruct.MappingTarget PopInventoryStat entity) {
        entity.setPopName(dto.getPopName());
        entity.setImportVolume(dto.getImportVolume());
        entity.setVolumeUsed(dto.getVolumeUsed());
        entity.setVolumeStocked(dto.getVolumeStocked());
    }
}