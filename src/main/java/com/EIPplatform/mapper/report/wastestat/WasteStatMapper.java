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

    @Mapping(target = "wasteType", source = "wasteType")
    @Mapping(target = "volumeCy", source = "volumeCy")
    @Mapping(target = "receiverOrg", source = "receiverOrg")
    @Mapping(target = "volumePy", source = "volumePy")
    WasteStat toEntity(WasteStatRequest request);

    @Mapping(target = "wasteType", source = "wasteType")
    @Mapping(target = "volumeCy", source = "volumeCy")
    @Mapping(target = "receiverOrg", source = "receiverOrg")
    @Mapping(target = "volumePy", source = "volumePy")
    void updateEntityFromRequest(WasteStatRequest request, @MappingTarget WasteStat entity);

    List<WasteStatDTO> toDTOList(List<WasteStat> entities);
}