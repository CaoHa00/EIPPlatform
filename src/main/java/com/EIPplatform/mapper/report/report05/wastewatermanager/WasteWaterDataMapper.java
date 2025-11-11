package com.EIPplatform.mapper.report.report05.wastewatermanager;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
import org.mapstruct.*;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        WasteWaterMonitoringExceedancesMapper.class,
        AutoWWMonitoringStatsMapper.class,
        AutoWWMonitoringIncidentsMapper.class,
        AutoWWQcvnExceedancesMapper.class
})
public interface WasteWaterDataMapper {

    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "connectionDiagram", ignore = true)
    @Mapping(target = "autoStationMap", ignore = true)
    @Mapping(target = "autoExceedSummary", ignore = true)
    @Mapping(target = "monitoringExceedances", source = "monitoringExceedances")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    WasteWaterData toEntity(WasteWaterDataCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "connectionDiagram", ignore = true)
    @Mapping(target = "autoStationMap", ignore = true)
    @Mapping(target = "monitoringExceedances", source = "monitoringExceedances")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    void updateEntityFromDto(WasteWaterDataCreateDTO dto, @MappingTarget WasteWaterData entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "connectionDiagram", ignore = true)
    @Mapping(target = "autoStationMap", ignore = true)
    @Mapping(target = "monitoringExceedances", source = "monitoringExceedances")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    void updateEntityFromDto(WasteWaterDataDTO dto, @MappingTarget WasteWaterData entity);

    @Mapping(source = "connectionDiagram", target = "connectionDiagram")
    @Mapping(source = "autoStationMap", target = "autoStationMap")
    @Mapping(target = "monitoringExceedances", source = "monitoringExceedances")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    WasteWaterDataDTO toDto(WasteWaterData entity);

    List<WasteWaterDataDTO> toDtoList(List<WasteWaterData> entities);

    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "connectionDiagram", ignore = true)
    @Mapping(target = "autoStationMap", ignore = true)
    @Mapping(target = "monitoringExceedances", source = "monitoringExceedances")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    WasteWaterData dtoToEntity(WasteWaterDataDTO dto);

    @AfterMapping
    default void handleNullLists(@MappingTarget WasteWaterData entity, WasteWaterDataCreateDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    @AfterMapping
    default void handleNullListsFromDto(@MappingTarget WasteWaterData entity, WasteWaterDataDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    @AfterMapping
    default void handleNullListsOnUpdateFromDto(@MappingTarget WasteWaterData entity, WasteWaterDataDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    default void initializeLists(WasteWaterData entity) {
        if (entity.getMonitoringExceedances() == null) {
            entity.setMonitoringExceedances(new ArrayList<>());
        }
        if (entity.getMonitoringStats() == null) {
            entity.setMonitoringStats(new ArrayList<>());
        }
        if (entity.getMonitoringIncidents() == null) {
            entity.setMonitoringIncidents(new ArrayList<>());
        }
        if (entity.getQcvnExceedances() == null) {
            entity.setQcvnExceedances(new ArrayList<>());
        }
    }

    default void setParentReferences(WasteWaterData entity) {
        if (entity.getMonitoringExceedances() != null) {
            entity.getMonitoringExceedances().forEach(child -> child.setWasteWaterData(entity));
        }
        if (entity.getMonitoringStats() != null) {
            entity.getMonitoringStats().forEach(child -> child.setWasteWaterData(entity));
        }
        if (entity.getMonitoringIncidents() != null) {
            entity.getMonitoringIncidents().forEach(child -> child.setWasteWaterData(entity));
        }
        if (entity.getQcvnExceedances() != null) {
            entity.getQcvnExceedances().forEach(child -> child.setWasteWaterData(entity));
        }
    }
}