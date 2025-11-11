package com.EIPplatform.mapper.report.report05.airemmissionmanagement;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringIncidentDTO; // Giả sử có Response DTO (tương tự CreateDTO + Long id)
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirAutoMonitoringIncident;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AirAutoMonitoringIncidentMapper {

    // Create DTO to Entity
    @Mapping(target = "airAutoMonitoringIncidentId", ignore = true) // Auto-generate ID
    @Mapping(target = "airEmissionData", ignore = true) // Set in service
    @Mapping(source = "incidentName", target = "incidentName")
    @Mapping(source = "incidentTime", target = "incidentTime")
    @Mapping(source = "incidentRemedy", target = "incidentRemedy") // Lob tự handle
    AirAutoMonitoringIncident toEntity(AirAutoMonitoringIncidentCreateDTO dto);

    // List mapping
    List<AirAutoMonitoringIncident> toEntityList(List<AirAutoMonitoringIncidentCreateDTO> dtos);

    // Entity to Response DTO
    @Mapping(source = "airAutoMonitoringIncidentId", target = "id") // Map ID to DTO's id
    @Mapping(source = "incidentName", target = "incidentName")
    @Mapping(source = "incidentTime", target = "incidentTime")
    @Mapping(source = "incidentRemedy", target = "incidentRemedy")
    AirAutoMonitoringIncidentDTO toDto(AirAutoMonitoringIncident entity);

    // List to DTO list
    List<AirAutoMonitoringIncidentDTO> toDtoList(List<AirAutoMonitoringIncident> entities);

    @AfterMapping
    default void setParent(@MappingTarget AirAutoMonitoringIncident entity, @Context AirEmissionData parent) {
        if (parent != null) {
            entity.setAirEmissionData(parent);
        }
    }
}