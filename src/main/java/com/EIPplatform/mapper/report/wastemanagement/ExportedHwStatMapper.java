package com.EIPplatform.mapper.report.wastemanagement;

import com.EIPplatform.model.dto.report.wastemanagement.exportedhwstat.ExportedHwStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.exportedhwstat.ExportedHwStatDTO;
import com.EIPplatform.model.entity.report.wastemanagement.ExportedHwStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExportedHwStatMapper {
    ExportedHwStatMapper INSTANCE = Mappers.getMapper(ExportedHwStatMapper.class);

    @Mapping(target = "exportedId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    @Mapping(source = "baselCode", target = "baselCode")
    ExportedHwStat toEntity(ExportedHwStatCreateDTO dto);

    @Mapping(source = "exportedId", target = "exportedId")
    ExportedHwStatDTO toDto(ExportedHwStat entity);

    List<ExportedHwStatDTO> toDtoList(List<ExportedHwStat> entities);

    default void updateFromDto(ExportedHwStatCreateDTO dto, @org.mapstruct.MappingTarget ExportedHwStat entity) {
        entity.setWasteName(dto.getWasteName());
        entity.setHwCode(dto.getHwCode());
        entity.setVolumeKg(dto.getVolumeKg());
        entity.setTransporterOrg(dto.getTransporterOrg());
        entity.setOverseasProcessorOrg(dto.getOverseasProcessorOrg());
    }
}