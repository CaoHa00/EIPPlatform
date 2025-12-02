package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat.DomesticSolidWasteStatUpdateDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.DomesticSolidWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DomesticSolidWasteStatMapper {

    DomesticSolidWasteStatMapper INSTANCE = Mappers.getMapper(DomesticSolidWasteStatMapper.class);

    @Mapping(target = "domesticId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    DomesticSolidWasteStat toEntity(DomesticSolidWasteStatCreateDTO dto);

    DomesticSolidWasteStatDTO toDto(DomesticSolidWasteStat entity);

    List<DomesticSolidWasteStatDTO> toDtoList(List<DomesticSolidWasteStat> entities);

    default void updateFromDto(
            DomesticSolidWasteStatUpdateDTO dto,
            @MappingTarget DomesticSolidWasteStat entity
    ) {
        if (dto.getWasteTypeName() != null)
            entity.setWasteTypeName(dto.getWasteTypeName());

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
