package com.EIPplatform.mapper.report.report05.airemmissionmanagement;


import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat.AirAutoMonitoringStatDTO; // Giả sử có Response DTO (tương tự CreateDTO + Long id)
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirAutoMonitoringStat;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AirAutoMonitoringStatMapper {

    @Mapping(target = "airAutoMonitoringStatId", ignore = true)
    @Mapping(target = "airEmissionData", ignore = true)
    @Mapping(source = "paramName", target = "paramName")
    @Mapping(source = "valDesign", target = "valDesign")
    @Mapping(source = "valReceived", target = "valReceived")
    @Mapping(source = "valError", target = "valError")
    @Mapping(source = "ratioReceivedDesign", target = "ratioReceivedDesign")
    @Mapping(source = "ratioErrorReceived", target = "ratioErrorReceived")
    AirAutoMonitoringStat toEntity(AirAutoMonitoringStatCreateDTO dto);

    List<AirAutoMonitoringStat> toEntityList(List<AirAutoMonitoringStatCreateDTO> dtos);

    // Entity to Response DTO
    @Mapping(source = "airAutoMonitoringStatId", target = "id")
    @Mapping(source = "paramName", target = "paramName")
    @Mapping(source = "valDesign", target = "valDesign")
    @Mapping(source = "valReceived", target = "valReceived")
    @Mapping(source = "valError", target = "valError")
    @Mapping(source = "ratioReceivedDesign", target = "ratioReceivedDesign")
    @Mapping(source = "ratioErrorReceived", target = "ratioErrorReceived")
    AirAutoMonitoringStatDTO toDto(AirAutoMonitoringStat entity);

    List<AirAutoMonitoringStatDTO> toDtoList(List<AirAutoMonitoringStat> entities);

    @AfterMapping
    default void setParent(@MappingTarget AirAutoMonitoringStat entity, @Context AirEmissionData parent) {
        if (parent != null) {
            entity.setAirEmissionData(parent);
        }
    }
}