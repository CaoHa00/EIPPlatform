package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import org.mapstruct.*;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {
        DomesticSolidWasteStatMapper.class,
        IndustrialSolidWasteStatMapper.class,
        RecycleIndustrialWasteStatMapper.class,
        OtherSolidWasteStatMapper.class,
        HazardousWasteStatMapper.class,
        ExportedHwStatMapper.class,
        SelfTreatedHwStatMapper.class,
        PopInventoryStatMapper.class
})
public interface WasteManagementDataMapper {

    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "domesticSolidWasteStats", source = "domesticSolidWasteStats")
    @Mapping(target = "industrialSolidWasteStats", source = "industrialSolidWasteStats")
    @Mapping(target = "recycleIndustrialWasteStats", source = "recycleIndustrialWasteStats")
    @Mapping(target = "otherSolidWasteStats", source = "otherSolidWasteStats")
    @Mapping(target = "hazardousWasteStats", source = "hazardousWasteStats")
    @Mapping(target = "exportedHwStats", source = "exportedHwStats")
    @Mapping(target = "selfTreatedHwStats", source = "selfTreatedHwStats")
    @Mapping(target = "popInventoryStats", source = "popInventoryStats")
    WasteManagementData toEntity(WasteManagementDataCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "domesticSolidWasteStats", source = "domesticSolidWasteStats")
    @Mapping(target = "industrialSolidWasteStats", source = "industrialSolidWasteStats")
    @Mapping(target = "recycleIndustrialWasteStats", source = "recycleIndustrialWasteStats")
    @Mapping(target = "otherSolidWasteStats", source = "otherSolidWasteStats")
    @Mapping(target = "hazardousWasteStats", source = "hazardousWasteStats")
    @Mapping(target = "exportedHwStats", source = "exportedHwStats")
    @Mapping(target = "selfTreatedHwStats", source = "selfTreatedHwStats")
    @Mapping(target = "popInventoryStats", source = "popInventoryStats")
    void updateEntityFromDto(WasteManagementDataCreateDTO dto, @MappingTarget WasteManagementData entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "domesticSolidWasteStats", source = "domesticSolidWasteStats")
    @Mapping(target = "industrialSolidWasteStats", source = "industrialSolidWasteStats")
    @Mapping(target = "recycleIndustrialWasteStats", source = "recycleIndustrialWasteStats")
    @Mapping(target = "otherSolidWasteStats", source = "otherSolidWasteStats")
    @Mapping(target = "hazardousWasteStats", source = "hazardousWasteStats")
    @Mapping(target = "exportedHwStats", source = "exportedHwStats")
    @Mapping(target = "selfTreatedHwStats", source = "selfTreatedHwStats")
    @Mapping(target = "popInventoryStats", source = "popInventoryStats")
    void updateEntityFromDto(WasteManagementDataDTO dto, @MappingTarget WasteManagementData entity);

    @Mapping(source = "wmId", target = "wmId")
    @Mapping(target = "domesticSolidWasteStats", source = "domesticSolidWasteStats")
    @Mapping(target = "industrialSolidWasteStats", source = "industrialSolidWasteStats")
    @Mapping(target = "recycleIndustrialWasteStats", source = "recycleIndustrialWasteStats")
    @Mapping(target = "otherSolidWasteStats", source = "otherSolidWasteStats")
    @Mapping(target = "hazardousWasteStats", source = "hazardousWasteStats")
    @Mapping(target = "exportedHwStats", source = "exportedHwStats")
    @Mapping(target = "selfTreatedHwStats", source = "selfTreatedHwStats")
    @Mapping(target = "popInventoryStats", source = "popInventoryStats")
    WasteManagementDataDTO toDto(WasteManagementData entity);

    List<WasteManagementDataDTO> toDtoList(List<WasteManagementData> entities);

    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "domesticSolidWasteStats", source = "domesticSolidWasteStats")
    @Mapping(target = "industrialSolidWasteStats", source = "industrialSolidWasteStats")
    @Mapping(target = "recycleIndustrialWasteStats", source = "recycleIndustrialWasteStats")
    @Mapping(target = "otherSolidWasteStats", source = "otherSolidWasteStats")
    @Mapping(target = "hazardousWasteStats", source = "hazardousWasteStats")
    @Mapping(target = "exportedHwStats", source = "exportedHwStats")
    @Mapping(target = "selfTreatedHwStats", source = "selfTreatedHwStats")
    @Mapping(target = "popInventoryStats", source = "popInventoryStats")
    WasteManagementData dtoToEntity(WasteManagementDataDTO dto);

    @AfterMapping
    default void handleNullLists(@MappingTarget WasteManagementData entity, WasteManagementDataCreateDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    @AfterMapping
    default void handleNullListsFromDto(@MappingTarget WasteManagementData entity, WasteManagementDataDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    @AfterMapping
    default void handleNullListsOnUpdateFromDto(@MappingTarget WasteManagementData entity, WasteManagementDataDTO dto) {
        initializeLists(entity);
        setParentReferences(entity);
    }

    default void initializeLists(WasteManagementData entity) {
        if (entity.getDomesticSolidWasteStats() == null) {
            entity.setDomesticSolidWasteStats(new ArrayList<>());
        }
        if (entity.getIndustrialSolidWasteStats() == null) {
            entity.setIndustrialSolidWasteStats(new ArrayList<>());
        }
        if (entity.getRecycleIndustrialWasteStats() == null) {
            entity.setRecycleIndustrialWasteStats(new ArrayList<>());
        }
        if (entity.getOtherSolidWasteStats() == null) {
            entity.setOtherSolidWasteStats(new ArrayList<>());
        }
        if (entity.getHazardousWasteStats() == null) {
            entity.setHazardousWasteStats(new ArrayList<>());
        }
        if (entity.getExportedHwStats() == null) {
            entity.setExportedHwStats(new ArrayList<>());
        }
        if (entity.getSelfTreatedHwStats() == null) {
            entity.setSelfTreatedHwStats(new ArrayList<>());
        }
        if (entity.getPopInventoryStats() == null) {
            entity.setPopInventoryStats(new ArrayList<>());
        }
    }

    default void setParentReferences(WasteManagementData entity) {
        if (entity.getDomesticSolidWasteStats() != null) {
            entity.getDomesticSolidWasteStats().forEach(child -> child.setWasteManagementData(entity));
        }
        if (entity.getIndustrialSolidWasteStats() != null) {
            entity.getIndustrialSolidWasteStats().forEach(child -> child.setWasteManagementData(entity));
        }
        if (entity.getRecycleIndustrialWasteStats() != null) {
            entity.getRecycleIndustrialWasteStats().forEach(child -> child.setWasteManagementData(entity));
        }
        if (entity.getOtherSolidWasteStats() != null) {
            entity.getOtherSolidWasteStats().forEach(child -> child.setWasteManagementData(entity));
        }
        if (entity.getHazardousWasteStats() != null) {
            entity.getHazardousWasteStats().forEach(child -> child.setWasteManagementData(entity));
        }
        if (entity.getExportedHwStats() != null) {
            entity.getExportedHwStats().forEach(child -> child.setWasteManagementData(entity));
        }
        if (entity.getSelfTreatedHwStats() != null) {
            entity.getSelfTreatedHwStats().forEach(child -> child.setWasteManagementData(entity));
        }
        if (entity.getPopInventoryStats() != null) {
            entity.getPopInventoryStats().forEach(child -> child.setWasteManagementData(entity));
        }
    }
}