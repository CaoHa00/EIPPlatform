package com.EIPplatform.mapper.report;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.model.dto.report.report.ReportA05DTO;
import com.EIPplatform.model.entity.report.ReportA05;

@Mapper(componentModel = "spring")
public interface ReportA05Mapper {
    
    @Mapping(source = "businessDetail.businessDetailId", target = "businessDetailId")
    @Mapping(source = "businessDetail.companyName", target = "companyName")
    ReportA05DTO toDTO(ReportA05 entity);
}