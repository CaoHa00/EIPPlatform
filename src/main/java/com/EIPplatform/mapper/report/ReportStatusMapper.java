package com.EIPplatform.mapper.report;

import org.mapstruct.Mapper;

import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import com.EIPplatform.model.entity.report.ReportStatus;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportStatusMapper {
    ReportStatusDTO toDTO(ReportStatus entity);

    ReportStatus toEntity(ReportStatusDTO dto);

    List<ReportStatusDTO> toDTOList(List<ReportStatus> entities);

    List<ReportStatus> toEntityList(List<ReportStatusDTO> dtos);
}