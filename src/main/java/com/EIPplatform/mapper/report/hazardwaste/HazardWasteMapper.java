package com.EIPplatform.mapper.report.hazardwaste;

import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteDTO;
import com.EIPplatform.model.dto.report.hazardwaste.HazardWasteRequest;
import com.EIPplatform.model.entity.report.HazardWaste;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HazardWasteMapper {

    @Mapping(target = "sectionId", source = "reportSection.sectionId")
    @Mapping(target = "reportId", source = "report.reportId")
    @Mapping(target = "sectionType", source = "reportSection.sectionType")
    HazardWasteDTO toDTO(HazardWaste entity);

    @Mapping(target = "wasteName", source = "wasteName")
    @Mapping(target = "hwCode", source = "hwCode")
    @Mapping(target = "volumeCy", source = "volumeCy")
    @Mapping(target = "treatmentMethod", source = "treatmentMethod")
    @Mapping(target = "receiverOrg", source = "receiverOrg")
    @Mapping(target = "volumePy", source = "volumePy")
    @Mapping(target = "sectionType", ignore = true)
    HazardWaste toEntity(HazardWasteRequest request);

    @Mapping(target = "wasteName", source = "wasteName")
    @Mapping(target = "hwCode", source = "hwCode")
    @Mapping(target = "volumeCy", source = "volumeCy")
    @Mapping(target = "treatmentMethod", source = "treatmentMethod")
    @Mapping(target = "receiverOrg", source = "receiverOrg")
    @Mapping(target = "volumePy", source = "volumePy")
    @Mapping(target = "sectionType", ignore = true)
    void updateEntityFromRequest(HazardWasteRequest request, @MappingTarget HazardWaste entity);

    List<HazardWasteDTO> toDTOList(List<HazardWaste> entities);

}