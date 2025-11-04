package com.EIPplatform.mapper.report.wastewatermanager;

import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsCreateDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringincidents.AutoWWMonitoringIncidentsDTO;
import com.EIPplatform.model.entity.report.wastewatermanager.AutoWWMonitoringIncidents;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutoWWMonitoringIncidentsMapper {
    AutoWWMonitoringIncidentsMapper INSTANCE = Mappers.getMapper(AutoWWMonitoringIncidentsMapper.class);

    @Mapping(target = "incidentId", ignore = true)
    @Mapping(target = "wasteWaterData", ignore = true) // Set in parent mapper
    AutoWWMonitoringIncidents toEntity(AutoWWMonitoringIncidentsCreateDTO dto);

    @Mapping(source = "incidentId", target = "incidentId")
    AutoWWMonitoringIncidentsDTO toDto(AutoWWMonitoringIncidents entity);

    List<AutoWWMonitoringIncidentsDTO> toDtoList(List<AutoWWMonitoringIncidents> entities);

    default void updateFromDto(AutoWWMonitoringIncidentsCreateDTO dto, @org.mapstruct.MappingTarget AutoWWMonitoringIncidents entity) {
        entity.setIncidentName(dto.getIncidentName());
        entity.setIncidentTime(dto.getIncidentTime());
        entity.setIncidentRemedy(dto.getIncidentRemedy());
    }
}