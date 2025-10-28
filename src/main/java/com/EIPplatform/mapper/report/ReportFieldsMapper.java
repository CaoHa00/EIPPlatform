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

    @Mapping(target = "fieldName", source = "fieldName")
    @Mapping(target = "fieldValue", source = "fieldValue")
    @Mapping(target = "createdAt", ignore = true)
    ReportFields toEntity(ReportFieldRequest request);

    @Mapping(target = "fieldName", source = "fieldName")
    @Mapping(target = "fieldValue", source = "fieldValue")
    void updateEntityFromRequest(ReportFieldRequest request, @MappingTarget ReportFields entity);

    List<ReportFieldDTO> toDTOList(List<ReportFields> entities);
}