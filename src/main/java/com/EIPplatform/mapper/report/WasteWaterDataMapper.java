package com.EIPplatform.mapper.report;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.WasteWaterData;

@Mapper(componentModel = "spring")
public interface WasteWaterDataMapper {
    
    
     @Mapping(source = "report.reportId", target = "reportId")
     WasteWaterDataDTO toDTO(WasteWaterData entity);
    
    
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "wwId", ignore = true)
    WasteWaterData toEntity(WasteWaterDataDTO dto);
    
    /**
     * Update existing entity from DTO (dùng cho update operation)
     */
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "wwId", ignore = true)
    void updateEntityFromDTO(ReportA05DraftDTO dto, @MappingTarget WasteWaterData entity);
}