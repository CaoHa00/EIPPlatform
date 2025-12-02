package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.HazardousWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HazardousWasteStatMapper {
    HazardousWasteStatMapper INSTANCE = Mappers.getMapper(HazardousWasteStatMapper.class);

    @Mapping(target = "hazardousId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    HazardousWasteStat toEntity(HazardousWasteStatCreateDTO dto);

    HazardousWasteStatDTO toDto(HazardousWasteStat entity);

    List<HazardousWasteStatDTO> toDtoList(List<HazardousWasteStat> entities);



    default void updateFromDto(
            HazardousWasteStatCreateDTO dto,
            @MappingTarget HazardousWasteStat entity
    ) {
        if (dto.getWasteName() != null)
            entity.setWasteName(dto.getWasteName());

        if (dto.getHwCode() != null)
            entity.setHwCode(dto.getHwCode());

        if (dto.getUnitCy() != null)
            entity.setUnitCy(dto.getUnitCy());

        if (dto.getVolumePy() != null)
            entity.setVolumeCy(dto.getVolumePy());

        if (dto.getTreatmentMethod() != null)
            entity.setTreatmentMethod(dto.getTreatmentMethod());

        if (dto.getReceiverOrg() != null)
            entity.setReceiverOrg(dto.getReceiverOrg());

        if (dto.getVolumePy() != null)
            entity.setVolumePy(dto.getVolumePy());

        if (dto.getUnitPy() != null)
            entity.setUnitPy(dto.getUnitPy());
    }
}