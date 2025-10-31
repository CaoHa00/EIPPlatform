package com.EIPplatform.mapper.report.wastemanagement;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataUpdateDTO;
import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;

@Mapper(componentModel = "spring")
public interface WasteManagementDataMapper {

    // Create DTO to Entity
    @Mapping(target = "wmId", ignore = true) // Auto-generate
    @Mapping(target = "report", ignore = true) // Set in service
    @Mapping(target = "domesticSolidWasteStats", ignore = true)
    @Mapping(target = "industrialSolidWasteStats", ignore = true)
    @Mapping(target = "recycleIndustrialWasteStats", ignore = true)
    @Mapping(target = "otherSolidWasteStats", ignore = true)
    @Mapping(target = "hazardousWasteStats", ignore = true)
    @Mapping(target = "exportedHwStats", ignore = true)
    @Mapping(target = "selfTreatedHwStats", ignore = true)
    @Mapping(target = "popInventoryStats", ignore = true)
    WasteManagementData toEntity(WasteManagementDataCreateDTO dto);

    // Update DTO to Entity (partial - ignore null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "domesticSolidWasteStats", ignore = true)
    @Mapping(target = "industrialSolidWasteStats", ignore = true)
    @Mapping(target = "recycleIndustrialWasteStats", ignore = true)
    @Mapping(target = "otherSolidWasteStats", ignore = true)
    @Mapping(target = "hazardousWasteStats", ignore = true)
    @Mapping(target = "exportedHwStats", ignore = true)
    @Mapping(target = "selfTreatedHwStats", ignore = true)
    @Mapping(target = "popInventoryStats", ignore = true)
    void updateEntityFromDto(WasteManagementDataUpdateDTO dto, @MappingTarget WasteManagementData entity);

    // Entity to Response DTO
    WasteManagementDataDTO toDto(WasteManagementData entity);

    // List to DTO list (if bulk needed)
    List<WasteManagementDataDTO> toDtoList(List<WasteManagementData> entities);

    @Mapping(target = "report", ignore = true) // Will be set separately
    WasteManagementData dtoToEntity(WasteManagementDataDTO dto);

    @AfterMapping
    default void handleNullLists(@MappingTarget WasteManagementData entity, WasteManagementDataCreateDTO dto) {
        // Init empty lists if null (avoid NPE)
        if (dto.getDomesticSolidWasteStats() == null) {
            entity.setDomesticSolidWasteStats(List.of());
        }
        if (dto.getIndustrialSolidWasteStats() == null) {
            entity.setIndustrialSolidWasteStats(List.of());
        }
        if (dto.getRecycleIndustrialWasteStats() == null) {
            entity.setRecycleIndustrialWasteStats(List.of());
        }
        if (dto.getOtherSolidWasteStats() == null) {
            entity.setOtherSolidWasteStats(List.of());
        }
        if (dto.getHazardousWasteStats() == null) {
            entity.setHazardousWasteStats(List.of());
        }
        if (dto.getExportedHwStats() == null) {
            entity.setExportedHwStats(List.of());
        }
        if (dto.getSelfTreatedHwStats() == null) {
            entity.setSelfTreatedHwStats(List.of());
        }
        if (dto.getPopInventoryStats() == null) {
            entity.setPopInventoryStats(List.of());
        }
    }
}