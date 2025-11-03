package com.EIPplatform.mapper.report.airemmissionmanagement;

import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataUpdateDTO;
import com.EIPplatform.model.entity.report.airemmissionmanagement.AirEmissionData;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AirEmissionDataMapper {

    // Create DTO to Entity
    @Mapping(target = "airEmissionDataId", ignore = true) // ✅ Sửa: Auto-generate (was "id")
    @Mapping(target = "report", ignore = true) // Set in service
    @Mapping(target = "airAutoStationMapFilePath", ignore = true) // ✅ Ignore file path (set in service)
    @Mapping(target = "airMonitoringExceedances", ignore = true)
    @Mapping(target = "airAutoMonitoringStats", ignore = true)
    @Mapping(target = "airAutoMonitoringIncidents", ignore = true)
    @Mapping(target = "airAutoQcvnExceedances", ignore = true)
    AirEmissionData toEntity(AirEmissionDataCreateDTO dto);

    // Update DTO to Entity (partial - ignore null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "airEmissionDataId", ignore = true) // ✅ Sửa: Ignore ID (was "id")
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "airAutoStationMapFilePath", ignore = true) // ✅ Ignore file path (set in service)
    @Mapping(target = "airMonitoringExceedances", ignore = true)
    @Mapping(target = "airAutoMonitoringStats", ignore = true)
    @Mapping(target = "airAutoMonitoringIncidents", ignore = true)
    @Mapping(target = "airAutoQcvnExceedances", ignore = true)
    void updateEntityFromDto(AirEmissionDataUpdateDTO dto, @MappingTarget AirEmissionData entity);

    // Entity to Response DTO
    @Mapping(source = "airEmissionDataId", target = "id") // ✅ Map ID từ entity sang DTO's id
    @Mapping(source = "airAutoStationMapFilePath", target = "airAutoStationMap") // ✅ Map file path (giả sử DTO có field tương ứng)
    AirEmissionDataDTO toDto(AirEmissionData entity);

    // List to DTO list (if bulk needed)
    List<AirEmissionDataDTO> toDtoList(List<AirEmissionData> entities);

    @Mapping(source = "id", target = "airEmissionDataId") // ✅ Map DTO's id → entity ID
    @Mapping(target = "report", ignore = true) // Will be set separately
    AirEmissionData dtoToEntity(AirEmissionDataDTO dto);

    @AfterMapping
    default void handleNullLists(@MappingTarget AirEmissionData entity, AirEmissionDataCreateDTO dto) {
        // Init empty lists if null (avoid NPE)
        if (dto.getAirMonitoringExceedances() == null) {
            entity.setAirMonitoringExceedances(List.of());
        }
        if (dto.getAirAutoMonitoringStats() == null) {
            entity.setAirAutoMonitoringStats(List.of());
        }
        if (dto.getAirAutoMonitoringIncidents() == null) {
            entity.setAirAutoMonitoringIncidents(List.of());
        }
        if (dto.getAirAutoQcvnExceedances() == null) {
            entity.setAirAutoQcvnExceedances(List.of());
        }
    }
}