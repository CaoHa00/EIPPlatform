package com.EIPplatform.mapper.report.monitorexceedance;

import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceDTO;
import com.EIPplatform.model.dto.report.monitorexceedance.MonitorExceedanceRequest;
import com.EIPplatform.model.entity.report.MonitorExceedance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MonitorExceedanceMapper {

    @Mapping(target = "sectionId", source = "reportSection.sectionId")
    @Mapping(target = "reportId", source = "report.reportId")
    @Mapping(target = "sectionType", source = "reportSection.sectionType")
    MonitorExceedanceDTO toDTO(MonitorExceedance entity);

    @Mapping(target = "exceedanceId", ignore = true)
    @Mapping(target = "reportSection", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "sectionType", ignore = true)
    MonitorExceedance toEntity(MonitorExceedanceRequest request);

    @Mapping(target = "exceedanceId", ignore = true)
    @Mapping(target = "reportSection", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "sectionType", ignore = true)
    void updateEntityFromRequest(MonitorExceedanceRequest request, @MappingTarget MonitorExceedance entity);

    List<MonitorExceedanceDTO> toDTOList(List<MonitorExceedance> entities);
}