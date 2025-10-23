package com.EIPplatform.mapper.report;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.EIPplatform.model.dto.report.ReportStatusDto;
import com.EIPplatform.model.entity.report.ReportStatus;

@Mapper(componentModel = "spring")
public interface ReportStatusMapper {
    
    ReportStatusMapper INSTANCE = Mappers.getMapper(ReportStatusMapper.class);
    
    // Entity to DTO
    ReportStatusDto toDTO(ReportStatus entity);
    
    // DTO to Entity
    ReportStatus toEntity(ReportStatusDto dto);
    
    // List mapping
    java.util.List<ReportStatusDto> toDTOList(java.util.List<ReportStatus> entities);
    
    java.util.List<ReportStatus> toEntityList(java.util.List<ReportStatusDto> dtos);
}