package com.EIPplatform.mapper.report.automonstat;

import com.EIPplatform.model.dto.report.automonstat.AutoMonStatDTO;
import com.EIPplatform.model.entity.report.AutoMonStat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AutoMonStatMapper {

    @Mapping(target = "reportId", source = "report.reportId")
    AutoMonStatDTO toDTO(AutoMonStat entity);

    @Mapping(target = "statId", ignore = true)
    @Mapping(target = "report", ignore = true)
    AutoMonStat toEntity(AutoMonStatDTO dto);

    List<AutoMonStatDTO> toDTOList(List<AutoMonStat> entities);

    List<AutoMonStat> toEntityList(List<AutoMonStatDTO> dtos);
}