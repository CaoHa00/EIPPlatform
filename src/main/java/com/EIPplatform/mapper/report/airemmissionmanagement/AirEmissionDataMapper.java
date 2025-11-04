package com.EIPplatform.mapper.report.airemmissionmanagement;

import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataUpdateDTO;
import com.EIPplatform.model.entity.report.airemmissionmanagement.AirEmissionData;
import org.mapstruct.*;
import java.util.List;
import java.util.ArrayList;

@Mapper(componentModel = "spring", uses = {
        AirMonitoringExceedanceMapper.class,
        AirAutoMonitoringStatMapper.class,
        AirAutoMonitoringIncidentMapper.class,
        AirAutoQcvnExceedanceMapper.class
})
public interface AirEmissionDataMapper {

    @Mapping(target = "airEmissionDataId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "airAutoStationMapFilePath", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "airMonitoringExceedances", source = "airMonitoringExceedances")
    @Mapping(target = "airAutoMonitoringStats", source = "airAutoMonitoringStats")
    @Mapping(target = "airAutoMonitoringIncidents", source = "airAutoMonitoringIncidents")
    @Mapping(target = "airAutoQcvnExceedances", source = "airAutoQcvnExceedances")
    AirEmissionData toEntity(AirEmissionDataCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "airEmissionDataId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "airAutoStationMapFilePath", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "airMonitoringExceedances", source = "airMonitoringExceedances")
    @Mapping(target = "airAutoMonitoringStats", source = "airAutoMonitoringStats")
    @Mapping(target = "airAutoMonitoringIncidents", source = "airAutoMonitoringIncidents")
    @Mapping(target = "airAutoQcvnExceedances", source = "airAutoQcvnExceedances")
    void updateEntityFromDto(AirEmissionDataUpdateDTO dto, @MappingTarget AirEmissionData entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "airEmissionDataId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "airAutoStationMapFilePath", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "airMonitoringExceedances", source = "airMonitoringExceedances")
    @Mapping(target = "airAutoMonitoringStats", source = "airAutoMonitoringStats")
    @Mapping(target = "airAutoMonitoringIncidents", source = "airAutoMonitoringIncidents")
    @Mapping(target = "airAutoQcvnExceedances", source = "airAutoQcvnExceedances")
    void updateEntityFromDto(AirEmissionDataDTO dto, @MappingTarget AirEmissionData entity);

    @Mapping(source = "airAutoStationMapFilePath", target = "airAutoStationMap")
    @Mapping(target = "airMonitoringExceedances", source = "airMonitoringExceedances")
    @Mapping(target = "airAutoMonitoringStats", source = "airAutoMonitoringStats")
    @Mapping(target = "airAutoMonitoringIncidents", source = "airAutoMonitoringIncidents")
    @Mapping(target = "airAutoQcvnExceedances", source = "airAutoQcvnExceedances")
    AirEmissionDataDTO toDto(AirEmissionData entity);

    List<AirEmissionDataDTO> toDtoList(List<AirEmissionData> entities);

    @Mapping(target = "airEmissionDataId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "airAutoStationMapFilePath", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AirEmissionData dtoToEntity(AirEmissionDataDTO dto);

    @AfterMapping
    default void handleNullLists(@MappingTarget AirEmissionData entity, AirEmissionDataCreateDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    @AfterMapping
    default void handleNullListsFromDto(@MappingTarget AirEmissionData entity, AirEmissionDataDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    @AfterMapping
    default void handleNullListsOnUpdateFromDto(@MappingTarget AirEmissionData entity, AirEmissionDataDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    default void initializeLists(AirEmissionData entity) {
        if (entity.getAirMonitoringExceedances() == null) {
            entity.setAirMonitoringExceedances(new ArrayList<>());
        }
        if (entity.getAirAutoMonitoringStats() == null) {
            entity.setAirAutoMonitoringStats(new ArrayList<>());
        }
        if (entity.getAirAutoMonitoringIncidents() == null) {
            entity.setAirAutoMonitoringIncidents(new ArrayList<>());
        }
        if (entity.getAirAutoQcvnExceedances() == null) {
            entity.setAirAutoQcvnExceedances(new ArrayList<>());
        }
    }

    default void setParentReferences(AirEmissionData entity) {
        if (entity.getAirMonitoringExceedances() != null) {
            entity.getAirMonitoringExceedances().forEach(child -> child.setAirEmissionData(entity));
        }
        if (entity.getAirAutoMonitoringStats() != null) {
            entity.getAirAutoMonitoringStats().forEach(child -> child.setAirEmissionData(entity));
        }
        if (entity.getAirAutoMonitoringIncidents() != null) {
            entity.getAirAutoMonitoringIncidents().forEach(child -> child.setAirEmissionData(entity));
        }
        if (entity.getAirAutoQcvnExceedances() != null) {
            entity.getAirAutoQcvnExceedances().forEach(child -> child.setAirEmissionData(entity));
        }
    }
}