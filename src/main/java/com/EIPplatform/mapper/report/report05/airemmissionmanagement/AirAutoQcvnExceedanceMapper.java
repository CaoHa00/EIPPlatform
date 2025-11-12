package com.EIPplatform.mapper.report.report05.airemmissionmanagement;

import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance.AirAutoQcvnExceedanceDTO; // Giả sử có Response DTO (tương tự CreateDTO + Long id)
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirAutoQcvnExceedance;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AirAutoQcvnExceedanceMapper {

    // Create DTO to Entity
    @Mapping(target = "airAutoQcvnExceedanceId", ignore = true) // Auto-generate ID
    @Mapping(target = "airEmissionData", ignore = true) // Set in service
    @Mapping(source = "paramName", target = "paramName")
    @Mapping(source = "exceedDaysCount", target = "exceedDaysCount")
    @Mapping(source = "qcvnLimitValue", target = "qcvnLimitValue")
    @Mapping(source = "exceedRatioPercent", target = "exceedRatioPercent") // BigDecimal tự handle
    AirAutoQcvnExceedance toEntity(AirAutoQcvnExceedanceCreateDTO dto);

    // List mapping
    List<AirAutoQcvnExceedance> toEntityList(List<AirAutoQcvnExceedanceCreateDTO> dtos);

    // Entity to Response DTO
    @Mapping(source = "airAutoQcvnExceedanceId", target = "id") // Map ID to DTO's id
    @Mapping(source = "paramName", target = "paramName")
    @Mapping(source = "exceedDaysCount", target = "exceedDaysCount")
    @Mapping(source = "qcvnLimitValue", target = "qcvnLimitValue")
    @Mapping(source = "exceedRatioPercent", target = "exceedRatioPercent")
    AirAutoQcvnExceedanceDTO toDto(AirAutoQcvnExceedance entity);

    // List to DTO list
    List<AirAutoQcvnExceedanceDTO> toDtoList(List<AirAutoQcvnExceedance> entities);

    @AfterMapping
    default void setParent(@MappingTarget AirAutoQcvnExceedance entity, @Context AirEmissionData parent) {
        if (parent != null) {
            entity.setAirEmissionData(parent);
        }
    }
}