package com.EIPplatform.mapper.report.wastemanagement;

import com.EIPplatform.model.dto.report.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatDTO;
import com.EIPplatform.model.entity.report.wastemanagement.DomesticSolidWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DomesticSolidWasteStatMapper {
    DomesticSolidWasteStatMapper INSTANCE = Mappers.getMapper(DomesticSolidWasteStatMapper.class);

    @Mapping(target = "domesticId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true) // Set in parent mapper
    DomesticSolidWasteStat toEntity(DomesticSolidWasteStatCreateDTO dto);

    @Mapping(source = "domesticId", target = "domesticId")
    DomesticSolidWasteStatDTO toDto(DomesticSolidWasteStat entity);

    List<DomesticSolidWasteStatDTO> toDtoList(List<DomesticSolidWasteStat> entities);

    default void updateFromDto(DomesticSolidWasteStatCreateDTO dto, @org.mapstruct.MappingTarget DomesticSolidWasteStat entity) {
        // Partial update logic if needed, but for simplicity, full replace
        entity.setWasteTypeName(dto.getWasteTypeName());
        entity.setVolumeCy(dto.getVolumeCy());
        entity.setReceiverOrg(dto.getReceiverOrg());
        entity.setVolumePy(dto.getVolumePy());
    }
}