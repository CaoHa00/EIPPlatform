package com.EIPplatform.mapper.report;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.model.dto.report.reportstatus.ReportStatusDTO;
import com.EIPplatform.model.entity.report.ReportStatus;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportStatusMapper {

    @Mapping(target = "statusColor", ignore = true)
    ReportStatusDTO toDTO(ReportStatus entity);

    @Mapping(target = "statusId", ignore = true)
    @Mapping(target = "reports", ignore = true)
    ReportStatus toEntity(ReportStatusDTO dto);

    List<ReportStatusDTO> toDTOList(List<ReportStatus> entities);
}