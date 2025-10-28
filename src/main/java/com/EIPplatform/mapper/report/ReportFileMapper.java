package com.EIPplatform.mapper.report;

import com.EIPplatform.model.dto.report.reportfile.ReportFileDTO;
import com.EIPplatform.model.dto.report.reportfile.ReportFileRequest;
import com.EIPplatform.model.entity.report.ReportFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportFileMapper {

    @Mapping(target = "reportId", source = "report.reportId")
    ReportFileDTO toDTO(ReportFile entity);

    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "uploadDate", ignore = true)
    ReportFile toEntity(ReportFileRequest request);

    @Mapping(target = "fileId", ignore = true)
    @Mapping(target = "report", ignore = true)
    @Mapping(target = "uploadDate", ignore = true)
    void updateEntityFromRequest(ReportFileRequest request, @MappingTarget ReportFile entity);

    List<ReportFileDTO> toDTOList(List<ReportFile> entities);
}