package com.EIPplatform.mapper.report.wastemanagement;

import com.EIPplatform.model.dto.report.wastemanagement.selftreatedhwstat.SelfTreatedHwStatCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.selftreatedhwstat.SelfTreatedHwStatDTO;
import com.EIPplatform.model.entity.report.wastemanagement.SelfTreatedHwStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SelfTreatedHwStatMapper {
    SelfTreatedHwStatMapper INSTANCE = Mappers.getMapper(SelfTreatedHwStatMapper.class);

    @Mapping(target = "selfTreatedId", ignore = true)
    @Mapping(target = "wasteManagementData", ignore = true)
    SelfTreatedHwStat toEntity(SelfTreatedHwStatCreateDTO dto);

    @Mapping(source = "selfTreatedId", target = "selfTreatedId")
    SelfTreatedHwStatDTO toDto(SelfTreatedHwStat entity);

    List<SelfTreatedHwStatDTO> toDtoList(List<SelfTreatedHwStat> entities);

    default void updateFromDto(SelfTreatedHwStatCreateDTO dto, @org.mapstruct.MappingTarget SelfTreatedHwStat entity) {
        entity.setWasteName(dto.getWasteName());
        entity.setHwCode(dto.getHwCode());
        entity.setVolumeKg(dto.getVolumeKg());
        entity.setSelfTreatmentMethod(dto.getSelfTreatmentMethod());
    }
}