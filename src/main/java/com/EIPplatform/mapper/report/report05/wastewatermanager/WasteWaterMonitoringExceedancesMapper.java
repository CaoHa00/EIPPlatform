package com.EIPplatform.mapper.report.report05.wastewatermanager;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.DomMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.IndMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.WasteWaterMonitoringExceedancesDTO;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterMonitoringExceedances;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WasteWaterMonitoringExceedancesMapper {
    WasteWaterMonitoringExceedancesMapper INSTANCE = Mappers.getMapper(WasteWaterMonitoringExceedancesMapper.class);

    // Map từ DomMonitoringExceedancesCreateDTO với wasteWaterType = DOMESTIC
    @Mapping(target = "exceedanceId", ignore = true)
    @Mapping(target = "wasteWaterData", ignore = true)
    @Mapping(target = "wasteWaterType", constant = "DOMESTIC")
    WasteWaterMonitoringExceedances domToEntity(DomMonitoringExceedancesCreateDTO dto);

    // Map từ IndMonitoringExceedancesCreateDTO với wasteWaterType = INDUSTRIAL
    @Mapping(target = "exceedanceId", ignore = true)
    @Mapping(target = "wasteWaterData", ignore = true)
    @Mapping(target = "wasteWaterType", constant = "INDUSTRIAL")
    WasteWaterMonitoringExceedances indToEntity(IndMonitoringExceedancesCreateDTO dto);

    // Map list cho DOMESTIC
    default List<WasteWaterMonitoringExceedances> domListToEntityList(List<DomMonitoringExceedancesCreateDTO> dtoList) {
        if (dtoList == null) {
            return new ArrayList<>();
        }
        return dtoList.stream()
                .map(this::domToEntity)
                .collect(Collectors.toList());
    }

    // Map list cho INDUSTRIAL
    default List<WasteWaterMonitoringExceedances> indListToEntityList(List<IndMonitoringExceedancesCreateDTO> dtoList) {
        if (dtoList == null) {
            return new ArrayList<>();
        }
        return dtoList.stream()
                .map(this::indToEntity)
                .collect(Collectors.toList());
    }

    @Mapping(source = "exceedanceId", target = "exceedanceId")
    WasteWaterMonitoringExceedancesDTO toDto(WasteWaterMonitoringExceedances entity);

    List<WasteWaterMonitoringExceedancesDTO> toDtoList(List<WasteWaterMonitoringExceedances> entities);

    // Update methods
    default void updateFromDomDto(DomMonitoringExceedancesCreateDTO dto, @MappingTarget WasteWaterMonitoringExceedances entity) {
        entity.setPointName(dto.getPointName());
        entity.setPointSymbol(dto.getPointSymbol());
        entity.setMonitoringDate(dto.getMonitoringDate());
        entity.setLongitude(dto.getLongitude());
        entity.setLatitude(dto.getLatitude());
        entity.setExceededParam(dto.getExceededParam());
        entity.setResultValue(dto.getResultValue());
        entity.setQcvnLimit(dto.getQcvnLimit());
        entity.setWasteWaterType(WasteWaterType.DOMESTIC);
    }

    default void updateFromIndDto(IndMonitoringExceedancesCreateDTO dto, @MappingTarget WasteWaterMonitoringExceedances entity) {
        entity.setPointName(dto.getPointName());
        entity.setPointSymbol(dto.getPointSymbol());
        entity.setMonitoringDate(dto.getMonitoringDate());
        entity.setLongitude(dto.getLongitude());
        entity.setLatitude(dto.getLatitude());
        entity.setExceededParam(dto.getExceededParam());
        entity.setResultValue(dto.getResultValue());
        entity.setQcvnLimit(dto.getQcvnLimit());
        entity.setWasteWaterType(WasteWaterType.INDUSTRIAL);
    }
}