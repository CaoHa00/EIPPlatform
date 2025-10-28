package com.EIPplatform.mapper.report;

import com.EIPplatform.model.dto.report.reporttype.ReportTypeDTO;
import com.EIPplatform.model.dto.report.reporttype.ReportTypeRequest;
import com.EIPplatform.model.entity.report.ReportType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ReportTemplateMapper.class})
public interface ReportTypeMapper {

    @Mapping(target = "templateName", source = "reportTemplate.templateName")
    ReportTypeDTO toDTO(ReportType entity);

    @Mapping(target = "reportName", source = "reportName")
    @Mapping(target = "reportTemplate.reportTemplateId", source = "reportTemplateId")
    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "frequency", source = "frequency")
    @Mapping(target = "description", source = "description")
    ReportType toEntity(ReportTypeRequest request);

    @Mapping(target = "reportName", source = "reportName")
    @Mapping(target = "reportTemplate.reportTemplateId", source = "reportTemplateId")
    @Mapping(target = "dueDate", source = "dueDate")
    @Mapping(target = "frequency", source = "frequency")
    @Mapping(target = "description", source = "description")
    void updateEntityFromRequest(ReportTypeRequest request, @MappingTarget ReportType entity);

    List<ReportTypeDTO> toDTOList(List<ReportType> entities);
}