package com.EIPplatform.mapper.report.report05.wastewatermanager;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterMonitoringExceedances;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WasteWaterMonitoringExceedancesMapper {
    WasteWaterMonitoringExceedancesMapper INSTANCE = Mappers.getMapper(WasteWaterMonitoringExceedancesMapper.class);

    @Mapping(target = "exceedanceId", ignore = true)
    @Mapping(target = "wasteWaterData", ignore = true)
    WasteWaterMonitoringExceedances toEntity(WasteWaterMonitoringExceedancesCreateDTO dto);

    @Mapping(source = "exceedanceId", target = "exceedanceId")
    WasteWaterMonitoringExceedancesDTO toDto(WasteWaterMonitoringExceedances entity);

    List<WasteWaterMonitoringExceedancesDTO> toDtoList(List<WasteWaterMonitoringExceedances> entities);

    default void updateFromDto(WasteWaterMonitoringExceedancesCreateDTO dto, @org.mapstruct.MappingTarget WasteWaterMonitoringExceedances entity) {
        entity.setPointName(dto.getPointName());
        entity.setPointSymbol(dto.getPointSymbol());
        entity.setMonitoringDate(dto.getMonitoringDate());
        entity.setLongitude(dto.getLongitude());
        entity.setLatitude(dto.getLatitude());
        entity.setExceededParam(dto.getExceededParam());
        entity.setResultValue(dto.getResultValue());
        entity.setQcvnLimit(dto.getQcvnLimit());
    }
}