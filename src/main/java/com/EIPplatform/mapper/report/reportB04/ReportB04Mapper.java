package com.EIPplatform.mapper.report.reportB04;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.model.dto.report.reportB04.ReportB04DTO;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
@Mapper(componentModel = "spring")
public interface ReportB04Mapper {
       // CreateRequest → Entity
    @Mapping(target = "reportInvestorDetail", ignore = true)
    ReportB04DTO toDTO(ReportB04 dto);
}
