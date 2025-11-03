package com.EIPplatform.mapper.report.wastewatermanager;


import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement.WasteWaterDataUpdateDTO;
import com.EIPplatform.model.entity.report.wastewatermanager.WasteWaterData;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WasteWaterDataMapper {

    // Create DTO to Entity
    @Mapping(target = "wwId", ignore = true) // Auto-generate
    @Mapping(target = "report", ignore = true) // Set in service
    @Mapping(target = "connectionDiagram", ignore = true) // File path set in service
    @Mapping(target = "autoStationMap", ignore = true) // File path set in service
    @Mapping(target = "autoExceedSummary", ignore = true) // File path set in service
    @Mapping(target = "monitoringExceedances", ignore = true)
    @Mapping(target = "monitoringStats", ignore = true)
    @Mapping(target = "monitoringIncidents", ignore = true)
    @Mapping(target = "qcvnExceedances", ignore = true)
    WasteWaterData toEntity(WasteWaterDataCreateDTO dto);

    // Update DTO to Entity (partial - ignore null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "connectionDiagram", ignore = true) // File path set in service
    @Mapping(target = "autoStationMap", ignore = true) // File path set in service
    @Mapping(target = "autoExceedSummary", ignore = true) // File path set in service
    @Mapping(target = "monitoringExceedances", ignore = true)
    @Mapping(target = "monitoringStats", ignore = true)
    @Mapping(target = "monitoringIncidents", ignore = true)
    @Mapping(target = "qcvnExceedances", ignore = true)
    void updateEntityFromDto(WasteWaterDataUpdateDTO dto, @MappingTarget WasteWaterData entity);

    // Entity to Response DTO
    @Mapping(source = "wwId", target = "wwId")
    @Mapping(source = "connectionDiagram", target = "connectionDiagram") // Include file path in response
    @Mapping(source = "autoStationMap", target = "autoStationMap") // Include file path in response
    @Mapping(source = "autoExceedSummary", target = "autoExceedSummary") // Include file path in response
    WasteWaterDataDTO toDto(WasteWaterData entity);

    // List to DTO list
    List<WasteWaterDataDTO> toDtoList(List<WasteWaterData> entities);

    @Mapping(target = "report", ignore = true) // Will be set separately
    WasteWaterData dtoToEntity(WasteWaterDataDTO dto);

    @AfterMapping
    default void handleNullLists(@MappingTarget WasteWaterData entity, WasteWaterDataCreateDTO dto) {
        // Init empty lists if null (avoid NPE)
        if (dto.getMonitoringExceedances() == null) {
            entity.setMonitoringExceedances(List.of());
        }
        if (dto.getMonitoringStats() == null) {
            entity.setMonitoringStats(List.of());
        }
        if (dto.getMonitoringIncidents() == null) {
            entity.setMonitoringIncidents(List.of());
        }
        if (dto.getQcvnExceedances() == null) {
            entity.setQcvnExceedances(List.of());
        }
    }
}