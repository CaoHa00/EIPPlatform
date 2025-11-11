package com.EIPplatform.mapper.report.report05.wastewatermanager;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwmonitoringstats.AutoWWMonitoringStatsDTO;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.AutoWWMonitoringStats;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutoWWMonitoringStatsMapper {
    AutoWWMonitoringStatsMapper INSTANCE = Mappers.getMapper(AutoWWMonitoringStatsMapper.class);

    @Mapping(target = "statId", ignore = true)
    @Mapping(target = "wasteWaterData", ignore = true)
    AutoWWMonitoringStats toEntity(AutoWWMonitoringStatsCreateDTO dto);

    @Mapping(source = "statId", target = "statId")
    AutoWWMonitoringStatsDTO toDto(AutoWWMonitoringStats entity);

    List<AutoWWMonitoringStatsDTO> toDtoList(List<AutoWWMonitoringStats> entities);

    default void updateFromDto(AutoWWMonitoringStatsCreateDTO dto, @org.mapstruct.MappingTarget AutoWWMonitoringStats entity) {
        entity.setParamName(dto.getParamName());
        entity.setValDesign(dto.getValDesign());
        entity.setValReceived(dto.getValReceived());
        entity.setValError(dto.getValError());
        entity.setRatioReceivedDesign(dto.getRatioReceivedDesign());
        entity.setRatioErrorReceived(dto.getRatioErrorReceived());
    }
}