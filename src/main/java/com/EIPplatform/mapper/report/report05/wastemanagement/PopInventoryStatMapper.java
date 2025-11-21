package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat.PopInventoryStatUpdateDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.PopInventoryStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PopInventoryStatMapper {

    PopInventoryStatMapper INSTANCE = Mappers.getMapper(PopInventoryStatMapper.class);

    @Mapping(target = "popInventoryId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    PopInventoryStat toEntity(PopInventoryStatCreateDTO dto);

    PopInventoryStatDTO toDto(PopInventoryStat entity);

    List<PopInventoryStatDTO> toDtoList(List<PopInventoryStat> entities);

    default void updateFromDto(PopInventoryStatUpdateDTO dto,
                               @MappingTarget PopInventoryStat entity) {

        if (dto.getPopName() != null)
            entity.setPopName(dto.getPopName());

        if (dto.getCasCode() != null)
            entity.setCasCode(dto.getCasCode());

        if (dto.getImportDate() != null)
            entity.setImportDate(dto.getImportDate());

        if (dto.getImportVolume() != null)
            entity.setImportVolume(dto.getImportVolume());

        if (dto.getImportUnit() != null)
            entity.setImportUnit(dto.getImportUnit());

        if (dto.getConcentration() != null)
            entity.setConcentration(dto.getConcentration());

        if (dto.getVolumeUsed() != null)
            entity.setVolumeUsed(dto.getVolumeUsed());

        if (dto.getUsedUnit() != null)
            entity.setUsedUnit(dto.getUsedUnit());

        if (dto.getVolumeStocked() != null)
            entity.setVolumeStocked(dto.getVolumeStocked());

        if (dto.getStockedUnit() != null)
            entity.setStockedUnit(dto.getStockedUnit());

        if (dto.getComplianceResult() != null)
            entity.setComplianceResult(dto.getComplianceResult());
    }

}