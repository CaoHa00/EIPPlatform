package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.exportedhwstat.ExportedHwStatUpdateDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.ExportedHwStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExportedHwStatMapper {
    ExportedHwStatMapper INSTANCE = Mappers.getMapper(ExportedHwStatMapper.class);

    @Mapping(target = "exportedId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    ExportedHwStat toEntity(ExportedHwStatCreateDTO dto);

    ExportedHwStatDTO toDto(ExportedHwStat entity);

    List<ExportedHwStatDTO> toDtoList(List<ExportedHwStat> entities);

    default void updateFromDto(
            ExportedHwStatUpdateDTO dto,
            @MappingTarget ExportedHwStat entity
    ) {
        if (dto.getWasteName() != null)
            entity.setWasteName(dto.getWasteName());

        if (dto.getHwCode() != null)
            entity.setHwCode(dto.getHwCode());

        if (dto.getBaselCode() != null)
            entity.setBaselCode(dto.getBaselCode());

        if (dto.getVolume() != null)
            entity.setVolume(dto.getVolume());

        if (dto.getUnit() != null)
            entity.setUnit(dto.getUnit());

        if (dto.getTransporterOrg() != null)
            entity.setTransporterOrg(dto.getTransporterOrg());

        if (dto.getOverseasProcessorOrg() != null)
            entity.setOverseasProcessorOrg(dto.getOverseasProcessorOrg());
    }
}