package com.EIPplatform.mapper.report.wastestat;

import com.EIPplatform.model.dto.report.wastestat.WasteStatDTO;
import com.EIPplatform.model.dto.report.wastestat.WasteStatRequest;
import com.EIPplatform.model.entity.report.WasteStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WasteStatMapper {

    @Mapping(target = "reportId", source = "report.reportId")
    WasteStatDTO toDTO(WasteStat entity);

    @Mapping(target = "statId", ignore = true)
    @Mapping(target = "report", ignore = true)
    WasteStat toEntity(WasteStatRequest request);

    @Mapping(target = "statId", ignore = true)
    @Mapping(target = "report", ignore = true)
    void updateEntityFromRequest(WasteStatRequest request, @MappingTarget WasteStat entity);

    List<WasteStatDTO> toDTOList(List<WasteStat> entities);
}