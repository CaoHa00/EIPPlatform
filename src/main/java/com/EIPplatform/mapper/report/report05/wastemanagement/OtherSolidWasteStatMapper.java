package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat.OtherSolidWasteStatUpdateDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.OtherSolidWasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    default void updateFromDto(
            OtherSolidWasteStatUpdateDTO dto,
            @MappingTarget OtherSolidWasteStat entity
    ) {
        if (dto.getWasteGroupOther() != null)
            entity.setWasteGroupOther(dto.getWasteGroupOther());

        if (dto.getVolumeCy() != null)
            entity.setVolumeCy(dto.getVolumeCy());

        if (dto.getUnitCy() != null)
            entity.setUnitCy(dto.getUnitCy());

        if (dto.getSelfTreatmentMethod() != null)
            entity.setSelfTreatmentMethod(dto.getSelfTreatmentMethod());

        if (dto.getReceiverOrg() != null)
            entity.setReceiverOrg(dto.getReceiverOrg());

        if (dto.getVolumePy() != null)
            entity.setVolumePy(dto.getVolumePy());

        if (dto.getUnitPy() != null)
            entity.setUnitPy(dto.getUnitPy());
    }

}