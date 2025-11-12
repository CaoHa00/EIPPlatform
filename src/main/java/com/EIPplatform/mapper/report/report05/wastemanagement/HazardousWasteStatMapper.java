package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat.HazardousWasteStatDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.HazardousWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HazardousWasteStatMapper {
    HazardousWasteStatMapper INSTANCE = Mappers.getMapper(HazardousWasteStatMapper.class);

    @Mapping(target = "hazardousId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    HazardousWasteStat toEntity(HazardousWasteStatCreateDTO dto);

    @Mapping(source = "hazardousId", target = "hazardousId")
    HazardousWasteStatDTO toDto(HazardousWasteStat entity);

    List<HazardousWasteStatDTO> toDtoList(List<HazardousWasteStat> entities);

    default void updateFromDto(HazardousWasteStatCreateDTO dto, @org.mapstruct.MappingTarget HazardousWasteStat entity) {
        entity.setWasteName(dto.getWasteName());
        entity.setHwCode(dto.getHwCode());
        entity.setVolumeCy(dto.getVolumeCy());
        entity.setTreatmentMethod(dto.getTreatmentMethod());
        entity.setReceiverOrg(dto.getReceiverOrg());
        entity.setVolumePy(dto.getVolumePy());
    }
}