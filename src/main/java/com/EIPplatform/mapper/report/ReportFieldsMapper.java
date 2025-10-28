package com.EIPplatform.mapper.report;

import com.EIPplatform.model.dto.report.reportfield.ReportFieldDTO;
import com.EIPplatform.model.dto.report.reportfield.ReportFieldRequest;
import com.EIPplatform.model.entity.report.ReportFields;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportFieldsMapper {

    @Mapping(target = "reportId", source = "report.reportId")
    ReportFieldDTO toDTO(ReportFields entity);

    @Mapping(target = "fieldId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ReportFields toEntity(ReportFieldRequest request);

    @Mapping(target = "fieldId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromRequest(ReportFieldRequest request, @MappingTarget ReportFields entity);

    List<ReportFieldDTO> toDTOList(List<ReportFields> entities);
}