package com.EIPplatform.mapper.report.report05.wastewatermanager;

import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.DomMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances.IndMonitoringExceedancesCreateDTO;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterMonitoringExceedances;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterType;
import org.mapstruct.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        WasteWaterMonitoringExceedancesMapper.class,
        AutoWWMonitoringStatsMapper.class,
        AutoWWMonitoringIncidentsMapper.class,
        AutoWWQcvnExceedancesMapper.class
})
public interface WasteWaterDataMapper {

    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "monitoringExceedances", expression = "java(mapMonitoringExceedances(dto))")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    WasteWaterData toEntity(WasteWaterDataCreateDTO dto);

    // Custom method để merge cả 2 list monitoring exceedances
    default List<WasteWaterMonitoringExceedances> mapMonitoringExceedances(WasteWaterDataCreateDTO dto) {
        List<WasteWaterMonitoringExceedances> result = new ArrayList<>();

        WasteWaterMonitoringExceedancesMapper mapper = WasteWaterMonitoringExceedancesMapper.INSTANCE;

        // Map DOMESTIC exceedances
        if (dto.getDomMonitoringExceedances() != null) {
            result.addAll(mapper.domListToEntityList(dto.getDomMonitoringExceedances()));
        }

        // Map INDUSTRIAL exceedances
        if (dto.getIndMonitoringExceedances() != null) {
            result.addAll(mapper.indListToEntityList(dto.getIndMonitoringExceedances()));
        }

        return result;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "monitoringExceedances", expression = "java(mapMonitoringExceedances(dto))")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    void updateEntityFromDto(WasteWaterDataCreateDTO dto, @MappingTarget WasteWaterData entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "monitoringExceedances", ignore = true) // Giữ nguyên vì DTO này đã có full data
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    void updateEntityFromDto(WasteWaterDataDTO dto, @MappingTarget WasteWaterData entity);

    // Convert entity sang DTO - tách ra 2 list theo type
    @Mapping(target = "domMonitoringExceedances", expression = "java(getDomesticExceedances(entity))")
    @Mapping(target = "indMonitoringExceedances", expression = "java(getIndustrialExceedances(entity))")
    @Mapping(target = "monitoringStats", source = "monitoringStats")
    @Mapping(target = "monitoringIncidents", source = "monitoringIncidents")
    @Mapping(target = "qcvnExceedances", source = "qcvnExceedances")
    WasteWaterDataDTO toDto(WasteWaterData entity);

    // Lấy domestic exceedances
    default List<DomMonitoringExceedancesCreateDTO> getDomesticExceedances(WasteWaterData entity) {
        if (entity.getMonitoringExceedances() == null) {
            return new ArrayList<>();
        }
        return entity.getMonitoringExceedances().stream()
                .filter(e -> e.getWasteWaterType() == WasteWaterType.DOMESTIC)
                .map(this::toDomDto)
                .collect(Collectors.toList());
    }

    // Lấy industrial exceedances
    default List<IndMonitoringExceedancesCreateDTO> getIndustrialExceedances(WasteWaterData entity) {
        if (entity.getMonitoringExceedances() == null) {
            return new ArrayList<>();
        }
        return entity.getMonitoringExceedances().stream()
                .filter(e -> e.getWasteWaterType() == WasteWaterType.INDUSTRIAL)
                .map(this::toIndDto)
                .collect(Collectors.toList());
    }

    // Helper methods để convert entity sang DTO
    default DomMonitoringExceedancesCreateDTO toDomDto(WasteWaterMonitoringExceedances entity) {
        return DomMonitoringExceedancesCreateDTO.builder()
                .pointName(entity.getPointName())
                .pointSymbol(entity.getPointSymbol())
                .monitoringDate(entity.getMonitoringDate())
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude())
                .exceededParam(entity.getExceededParam())
                .resultValue(entity.getResultValue())
                .qcvnLimit(entity.getQcvnLimit())
                .build();
    }

    default IndMonitoringExceedancesCreateDTO toIndDto(WasteWaterMonitoringExceedances entity) {
        return IndMonitoringExceedancesCreateDTO.builder()
                .pointName(entity.getPointName())
                .pointSymbol(entity.getPointSymbol())
                .monitoringDate(entity.getMonitoringDate())
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude())
                .exceededParam(entity.getExceededParam())
                .resultValue(entity.getResultValue())
                .qcvnLimit(entity.getQcvnLimit())
                .build();
    }

    List<WasteWaterDataDTO> toDtoList(List<WasteWaterData> entities);

    @Mapping(target = "wwId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "monitoringExceedances", ignore = true)
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