package com.EIPplatform.mapper.report.report05;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.model.dto.report.report05.ReportA05DTO;
import com.EIPplatform.model.entity.report.report05.ReportA05;

@Mapper(componentModel = "spring")
public interface ReportA05Mapper {
    
    @Mapping(source = "businessDetail.businessDetailId", target = "businessDetailId")
    @Mapping(source = "businessDetail.facilityName", target = "facilityName")
    ReportA05DTO toDTO(ReportA05 entity);
}