package com.EIPplatform.mapper.report;

import com.EIPplatform.model.dto.report.reporttype.ReportTypeDTO;
import com.EIPplatform.model.dto.report.reporttype.ReportTypeRequest;
import com.EIPplatform.model.entity.report.ReportType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportTypeMapper {

    @Mapping(target = "reportTemplateId", source = "reportTemplate.reportTemplateId")
    @Mapping(target = "templateName", source = "reportTemplate.templateName")
    ReportTypeDTO toDTO(ReportType entity);

    @Mapping(target = "reportTypeId", ignore = true)
    @Mapping(target = "reportTemplate", ignore = true)
    @Mapping(target = "reports", ignore = true)
    ReportType toEntity(ReportTypeRequest request);

    @Mapping(target = "reportTypeId", ignore = true)
    @Mapping(target = "reportTemplate", ignore = true)
    @Mapping(target = "reports", ignore = true)
    void updateEntityFromRequest(ReportTypeRequest request, @MappingTarget ReportType entity);

    List<ReportTypeDTO> toDTOList(List<ReportType> entities);
}