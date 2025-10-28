package com.EIPplatform.mapper.report;

import com.EIPplatform.model.dto.report.reporttemplate.ReportTemplateDTO;
import com.EIPplatform.model.dto.report.reporttemplate.ReportTemplateRequest;
import com.EIPplatform.model.entity.report.ReportTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportTemplateMapper {

    ReportTemplateDTO toDTO(ReportTemplate entity);

    @Mapping(target = "reportTemplateId", ignore = true)
    @Mapping(target = "reportTypes", ignore = true)
    ReportTemplate toEntity(ReportTemplateRequest request);

    @Mapping(target = "reportTemplateId", ignore = true)
    @Mapping(target = "reportTypes", ignore = true)
    void updateEntityFromRequest(ReportTemplateRequest request, @MappingTarget ReportTemplate entity);

    List<ReportTemplateDTO> toDTOList(List<ReportTemplate> entities);
}