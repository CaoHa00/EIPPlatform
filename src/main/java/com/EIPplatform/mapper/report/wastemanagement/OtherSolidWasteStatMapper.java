package com.EIPplatform.mapper.report.wastemanagement;

import com.EIPplatform.model.dto.report.wastemanagement.othersolidwastestat.OtherSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.othersolidwastestat.OtherSolidWasteStatDTO;
import com.EIPplatform.model.entity.report.wastemanagement.OtherSolidWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OtherSolidWasteStatMapper {
    OtherSolidWasteStatMapper INSTANCE = Mappers.getMapper(OtherSolidWasteStatMapper.class);

    @Mapping(target = "otherId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    @Mapping(source = "selfTreatmentMethod", target = "selfTreatmentMethod")
    @Mapping(source = "receiverOrg", target = "receiverOrg")
    OtherSolidWasteStat toEntity(OtherSolidWasteStatCreateDTO dto);

    @Mapping(source = "otherId", target = "otherId")
    OtherSolidWasteStatDTO toDto(OtherSolidWasteStat entity);

    List<OtherSolidWasteStatDTO> toDtoList(List<OtherSolidWasteStat> entities);

    default void updateFromDto(OtherSolidWasteStatCreateDTO dto, @org.mapstruct.MappingTarget OtherSolidWasteStat entity) {
        entity.setWasteGroupOther(dto.getWasteGroupOther());
        entity.setVolumeCy(dto.getVolumeCy());
        entity.setVolumePy(dto.getVolumePy());
    }
}