package com.EIPplatform.mapper.report;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportDTO;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.report.ReportStatus;
import com.EIPplatform.model.entity.report.ReportType;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    ReportDTO toBasicDTO(Report report);
    ReportDTO toFullDTO(Report report);
    Report toEntity(CreateReportRequest request, BusinessDetail businessDetail,
                    ReportType reportType, ReportStatus status, String reportCode);

    @Named("toBasicDTOList")
    default List<ReportDTO> toDTOList(List<Report> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toBasicDTO)
                .toList();
    }

    @Named("toFullDTOList")
    default List<ReportDTO> toFullDTOList(List<Report> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream()
                .map(this::toFullDTO)
                .toList();
    }
}