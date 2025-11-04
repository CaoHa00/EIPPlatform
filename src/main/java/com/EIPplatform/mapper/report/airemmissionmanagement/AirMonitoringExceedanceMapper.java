package com.EIPplatform.mapper.report.airemmissionmanagement;

import com.EIPplatform.model.dto.report.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceCreateDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airmonitoringexceedance.AirMonitoringExceedanceDTO; // Giả sử có Response DTO (tương tự CreateDTO + Long id)
import com.EIPplatform.model.entity.report.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.airemmissionmanagement.AirMonitoringExceedance;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AirMonitoringExceedanceMapper {

    // Create DTO to Entity
    @Mapping(target = "airMonitoringExceedanceId", ignore = true)
    @Mapping(target = "airEmissionData", ignore = true)
    @Mapping(source = "pointName", target = "pointName")
    @Mapping(source = "pointSymbol", target = "pointSymbol")
    @Mapping(source = "monitoringDate", target = "monitoringDate")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "exceededParam", target = "exceededParam")
    @Mapping(source = "resultValue", target = "resultValue") // BigDecimal tự handle precision
    @Mapping(source = "qcvnLimit", target = "qcvnLimit") // BigDecimal tự handle
    AirMonitoringExceedance toEntity(AirMonitoringExceedanceCreateDTO dto);

    // List mapping
    List<AirMonitoringExceedance> toEntityList(List<AirMonitoringExceedanceCreateDTO> dtos);

    // Entity to Response DTO
    @Mapping(source = "airMonitoringExceedanceId", target = "id") // Map ID to DTO's id
    @Mapping(source = "pointName", target = "pointName")
    @Mapping(source = "pointSymbol", target = "pointSymbol")
    @Mapping(source = "monitoringDate", target = "monitoringDate")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "exceededParam", target = "exceededParam")
    @Mapping(source = "resultValue", target = "resultValue")
    @Mapping(source = "qcvnLimit", target = "qcvnLimit")
    AirMonitoringExceedanceDTO toDto(AirMonitoringExceedance entity);

    // List to DTO list
    List<AirMonitoringExceedanceDTO> toDtoList(List<AirMonitoringExceedance> entities);

    // Optional: AfterMapping to set parent (sử dụng @Context nếu cần, nhưng khuyến nghị set trong service)
    @AfterMapping
    default void setParent(@MappingTarget AirMonitoringExceedance entity, @Context AirEmissionData parent) {
        if (parent != null) {
            entity.setAirEmissionData(parent);
        }
    }
}