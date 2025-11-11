package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.PopInventoryStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PopInventoryStatMapper {
    PopInventoryStatMapper INSTANCE = Mappers.getMapper(PopInventoryStatMapper.class);

    @Mapping(target = "popInventoryId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    @Mapping(source = "casCode", target = "casCode")
    @Mapping(source = "importDate", target = "importDate")
    @Mapping(source = "concentration", target = "concentration")
    @Mapping(source = "complianceResult", target = "complianceResult")
    PopInventoryStat toEntity(PopInventoryStatCreateDTO dto);

    @Mapping(source = "popInventoryId", target = "popInventoryId")
    PopInventoryStatDTO toDto(PopInventoryStat entity);

    List<PopInventoryStatDTO> toDtoList(List<PopInventoryStat> entities);

    default void updateFromDto(PopInventoryStatCreateDTO dto, @org.mapstruct.MappingTarget PopInventoryStat entity) {
        entity.setPopName(dto.getPopName());
        entity.setCasCode(dto.getCasCode());
        entity.setImportDate(dto.getImportDate());
        entity.setConcentration(dto.getConcentration());
        entity.setImportVolume(dto.getImportVolume());
        entity.setVolumeUsed(dto.getVolumeUsed());
        entity.setVolumeStocked(dto.getVolumeStocked());
        entity.setComplianceResult(dto.getComplianceResult());
    }
}