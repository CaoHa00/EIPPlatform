package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat.SelfTreatedHwStatUpdateDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.SelfTreatedHwStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SelfTreatedHwStatMapper {

    SelfTreatedHwStatMapper INSTANCE = Mappers.getMapper(SelfTreatedHwStatMapper.class);

    @Mapping(target = "selfTreatedId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    SelfTreatedHwStat toEntity(SelfTreatedHwStatCreateDTO dto);

    SelfTreatedHwStatDTO toDto(SelfTreatedHwStat entity);

    List<SelfTreatedHwStatDTO> toDtoList(List<SelfTreatedHwStat> entities);

    default void updateFromDto(SelfTreatedHwStatUpdateDTO dto,
                               @MappingTarget SelfTreatedHwStat entity) {

        if (dto.getWasteName() != null)
            entity.setWasteName(dto.getWasteName());

        if (dto.getHwCode() != null)
            entity.setHwCode(dto.getHwCode());

        if (dto.getVolume() != null)
            entity.setVolume(dto.getVolume());

        if (dto.getUnit() != null)
            entity.setUnit(dto.getUnit());

        if (dto.getSelfTreatmentMethod() != null)
            entity.setSelfTreatmentMethod(dto.getSelfTreatmentMethod());
    }
}