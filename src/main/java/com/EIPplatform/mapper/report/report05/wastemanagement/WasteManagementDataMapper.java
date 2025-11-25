package com.EIPplatform.mapper.report.report05.wastemanagement;

import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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

    WasteManagementDataMapper INSTANCE = Mappers.getMapper(WasteManagementDataMapper.class);

    // ==================== CREATE: DTO → Entity ====================
    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(source = "waterTotalVolume", target = "waterTotalVolume")
    @Mapping(source = "waterTotalUnit", target = "waterTotalUnit")
    @Mapping(source = "airTotalVolume", target = "airTotalVolume")
    @Mapping(source = "airTotalUnit", target = "airTotalUnit")
    @Mapping(source = "soilTotalVolume", target = "soilTotalVolume")
    @Mapping(source = "soilTotalUnit", target = "soilTotalUnit")
    @Mapping(source = "sewageSludgeTotalVolume", target = "sewageSludgeTotalVolume")
    @Mapping(source = "sewageSludgeTotalUnit", target = "sewageSludgeTotalUnit")
    @Mapping(source = "hwOnsiteTotalVolume", target = "hwOnsiteTotalVolume")
    @Mapping(source = "hwOnsiteTotalUnit", target = "hwOnsiteTotalUnit")
    @Mapping(source = "hwRecycleTotalVolume", target = "hwRecycleTotalVolume")
    @Mapping(source = "hwRecycleTotalUnit", target = "hwRecycleTotalUnit")
    @Mapping(source = "hwDisposalTotalVolume", target = "hwDisposalTotalVolume")
    @Mapping(source = "hwDisposalTotalUnit", target = "hwDisposalTotalUnit")
    WasteManagementData toEntity(WasteManagementDataCreateDTO dto);

    // ==================== Entity → DTO (Response) ====================
    @Mapping(source = "waterTotalVolume", target = "waterTotalVolume")
    @Mapping(source = "waterTotalUnit", target = "waterTotalUnit")
    @Mapping(source = "airTotalVolume", target = "airTotalVolume")
    @Mapping(source = "airTotalUnit", target = "airTotalUnit")
    @Mapping(source = "soilTotalVolume", target = "soilTotalVolume")
    @Mapping(source = "soilTotalUnit", target = "soilTotalUnit")
    @Mapping(source = "sewageSludgeTotalVolume", target = "sewageSludgeTotalVolume")
    @Mapping(source = "sewageSludgeTotalUnit", target = "sewageSludgeTotalUnit")
    @Mapping(source = "hwOnsiteTotalVolume", target = "hwOnsiteTotalVolume")
    @Mapping(source = "hwOnsiteTotalUnit", target = "hwOnsiteTotalUnit")
    @Mapping(source = "hwRecycleTotalVolume", target = "hwRecycleTotalVolume")
    @Mapping(source = "hwRecycleTotalUnit", target = "hwRecycleTotalUnit")
    @Mapping(source = "hwDisposalTotalVolume", target = "hwDisposalTotalVolume")
    @Mapping(source = "hwDisposalTotalUnit", target = "hwDisposalTotalUnit")
    WasteManagementDataDTO toDto(WasteManagementData entity);

    List<WasteManagementDataDTO> toDtoList(List<WasteManagementData> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(source = "waterTotalVolume", target = "waterTotalVolume")
    @Mapping(source = "waterTotalUnit", target = "waterTotalUnit")
    @Mapping(source = "airTotalVolume", target = "airTotalVolume")
    @Mapping(source = "airTotalUnit", target = "airTotalUnit")
    @Mapping(source = "soilTotalVolume", target = "soilTotalVolume")
    @Mapping(source = "soilTotalUnit", target = "soilTotalUnit")
    @Mapping(source = "sewageSludgeTotalVolume", target = "sewageSludgeTotalVolume")
    @Mapping(source = "sewageSludgeTotalUnit", target = "sewageSludgeTotalUnit")
    @Mapping(source = "hwOnsiteTotalVolume", target = "hwOnsiteTotalVolume")
    @Mapping(source = "hwOnsiteTotalUnit", target = "hwOnsiteTotalUnit")
    @Mapping(source = "hwRecycleTotalVolume", target = "hwRecycleTotalVolume")
    @Mapping(source = "hwRecycleTotalUnit", target = "hwRecycleTotalUnit")
    @Mapping(source = "hwDisposalTotalVolume", target = "hwDisposalTotalVolume")
    @Mapping(source = "hwDisposalTotalUnit", target = "hwDisposalTotalUnit")
    void updateEntityFromDto(WasteManagementDataCreateDTO dto, @MappingTarget WasteManagementData entity);

    // THÊM METHOD NÀY VÀO WasteManagementDataMapper (giữ nguyên tên hàm cũ)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(source = "waterTotalVolume", target = "waterTotalVolume")
    @Mapping(source = "waterTotalUnit", target = "waterTotalUnit")
    @Mapping(source = "airTotalVolume", target = "airTotalVolume")
    @Mapping(source = "airTotalUnit", target = "airTotalUnit")
    @Mapping(source = "soilTotalVolume", target = "soilTotalVolume")
    @Mapping(source = "soilTotalUnit", target = "soilTotalUnit")
    @Mapping(source = "sewageSludgeTotalVolume", target = "sewageSludgeTotalVolume")
    @Mapping(source = "sewageSludgeTotalUnit", target = "sewageSludgeTotalUnit")
    @Mapping(source = "hwOnsiteTotalVolume", target = "hwOnsiteTotalVolume")
    @Mapping(source = "hwOnsiteTotalUnit", target = "hwOnsiteTotalUnit")
    @Mapping(source = "hwRecycleTotalVolume", target = "hwRecycleTotalVolume")
    @Mapping(source = "hwRecycleTotalUnit", target = "hwRecycleTotalUnit")
    @Mapping(source = "hwDisposalTotalVolume", target = "hwDisposalTotalVolume")
    @Mapping(source = "hwDisposalTotalUnit", target = "hwDisposalTotalUnit")
    void updateEntityFromDto(WasteManagementDataDTO dto, @MappingTarget WasteManagementData entity);

    @Mapping(target = "wmId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(source = "waterTotalVolume", target = "waterTotalVolume")
    @Mapping(source = "waterTotalUnit", target = "waterTotalUnit")
    @Mapping(source = "airTotalVolume", target = "airTotalVolume")
    @Mapping(source = "airTotalUnit", target = "airTotalUnit")
    @Mapping(source = "soilTotalVolume", target = "soilTotalVolume")
    @Mapping(source = "soilTotalUnit", target = "soilTotalUnit")
    @Mapping(source = "sewageSludgeTotalVolume", target = "sewageSludgeTotalVolume")
    @Mapping(source = "sewageSludgeTotalUnit", target = "sewageSludgeTotalUnit")
    @Mapping(source = "hwOnsiteTotalVolume", target = "hwOnsiteTotalVolume")
    @Mapping(source = "hwOnsiteTotalUnit", target = "hwOnsiteTotalUnit")
    @Mapping(source = "hwRecycleTotalVolume", target = "hwRecycleTotalVolume")
    @Mapping(source = "hwRecycleTotalUnit", target = "hwRecycleTotalUnit")
    @Mapping(source = "hwDisposalTotalVolume", target = "hwDisposalTotalVolume")
    @Mapping(source = "hwDisposalTotalUnit", target = "hwDisposalTotalUnit")
    WasteManagementData dtoToEntity(WasteManagementDataDTO dto);

    @AfterMapping
    default void linkChildren(@MappingTarget WasteManagementData entity) {
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