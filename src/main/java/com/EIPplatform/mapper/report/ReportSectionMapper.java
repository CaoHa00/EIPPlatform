package com.EIPplatform.mapper.report;

import com.EIPplatform.mapper.report.hazardwaste.HazardWasteMapper;
import com.EIPplatform.mapper.report.monitorexceedance.MonitorExceedanceMapper;
import com.EIPplatform.model.dto.report.reportsection.ReportSectionDTO;
import com.EIPplatform.model.dto.report.reportsection.ReportSectionRequest;
import com.EIPplatform.model.entity.report.ReportSection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MonitorExceedanceMapper.class, HazardWasteMapper.class})
public interface ReportSectionMapper {

    @Mapping(target = "reportId", source = "report.reportId")
    @Mapping(target = "exceedances", source = "monitorExceedances")
    @Mapping(target = "hazardWastes", source = "hazardWastes")
    ReportSectionDTO toDTO(ReportSection entity);

    @Mapping(target = "sectionId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "version", constant = "1")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "monitorExceedances", ignore = true)
    @Mapping(target = "hazardWastes", ignore = true)
    ReportSection toEntity(ReportSectionRequest request);

    @Mapping(target = "sectionId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "monitorExceedances", ignore = true)
    @Mapping(target = "hazardWastes", ignore = true)
    void updateEntityFromRequest(ReportSectionRequest request, @MappingTarget ReportSection entity);

    List<ReportSectionDTO> toDTOList(List<ReportSection> entities);
}