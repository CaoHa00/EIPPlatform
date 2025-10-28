package com.EIPplatform.mapper.report;

import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportDTO;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.user.userInformation.BusinessDetail;
import com.EIPplatform.model.entity.report.ReportType;
import com.EIPplatform.model.entity.report.ReportStatus;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(source = "businessDetail.bussinessDetailId", target = "businessDetailId")
    @Mapping(source = "businessDetail.companyName", target = "businessName")
    @Mapping(source = "businessDetail.taxCode", target = "taxCode")
    @Mapping(source = "reportType", target = "reportType")
    @Mapping(source = "reportStatus", target = "reportStatus")
    @Mapping(source = "submittedBy.userAccountId", target = "submittedById")
    @Mapping(source = "submittedBy.email", target = "submittedByEmail")
    @Mapping(source = "submittedBy.fullName", target = "submittedByName")
    @Mapping(source = "reviewedBy.userAccountId", target = "reviewedById")
    @Mapping(source = "reviewedBy.email", target = "reviewedByEmail")
    @Mapping(source = "reviewedBy.fullName", target = "reviewedByName")
    @Mapping(source = "parentReport.reportId", target = "parentReportId")
    @Mapping(target = "fields", ignore = true)
    @Mapping(target = "sections", ignore = true)
    @Mapping(target = "files", ignore = true)
    ReportDTO toBasicDTO(Report report);

    @Mapping(source = "businessDetail.bussinessDetailId", target = "businessDetailId")
    @Mapping(source = "businessDetail.companyName", target = "businessName")
    @Mapping(source = "businessDetail.taxCode", target = "taxCode")
    @Mapping(source = "reportType", target = "reportType")
    @Mapping(source = "reportStatus", target = "reportStatus")
    @Mapping(source = "submittedBy.userAccountId", target = "submittedById")
    @Mapping(source = "submittedBy.email", target = "submittedByEmail")
    @Mapping(source = "submittedBy.fullName", target = "submittedByName")
    @Mapping(source = "reviewedBy.userAccountId", target = "reviewedById")
    @Mapping(source = "reviewedBy.email", target = "reviewedByEmail")
    @Mapping(source = "reviewedBy.fullName", target = "reviewedByName")
    @Mapping(source = "parentReport.reportId", target = "parentReportId")
    @Mapping(source = "reportFields", target = "fields")
    @Mapping(source = "reportSections", target = "sections")
    @Mapping(source = "reportFiles", target = "files")
    ReportDTO toFullDTO(Report report);

    @Mapping(target = "reportId", ignore = true)
    @Mapping(target = "reportCode", source = "reportCode")
    @Mapping(target = "businessDetail", source = "businessDetail")
    @Mapping(target = "reportType", source = "reportType")
    @Mapping(target = "reportYear", source = "request.reportYear")
    @Mapping(target = "reportingPeriod", source = "request.reportingPeriod")
    @Mapping(target = "reportStatus", source = "status")
    @Mapping(target = "completionPercentage", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "version", constant = "1")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "parentReport", ignore = true)
    @Mapping(target = "submittedBy", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    @Mapping(target = "reviewNotes", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "reportFields", ignore = true)
    @Mapping(target = "reportSections", ignore = true)
    @Mapping(target = "reportFiles", ignore = true)
    @Mapping(target = "monitorExceedances", ignore = true)
    @Mapping(target = "wasteStats", ignore = true)
    @Mapping(target = "hazardWastes", ignore = true)
    @Mapping(target = "autoMonStats", ignore = true)
    @Mapping(target = "childReports", ignore = true)
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