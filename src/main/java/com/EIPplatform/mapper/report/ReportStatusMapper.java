package com.EIPplatform.mapper.report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import com.EIPplatform.model.entity.report.ReportStatus;

@Mapper(componentModel = "spring")
public interface ReportStatusMapper {

    ReportStatusDTO toDTO(ReportStatus entity);
    @Mapping(target = "statusId", ignore = true)
    ReportStatus toEntity(ReportStatusDTO dto);

    List<ReportStatusDTO> toDTOList(List<ReportStatus> entities);
}