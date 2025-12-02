package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.facility.FacilityResponseDto;
import com.EIPplatform.model.entity.businessInformation.Facility;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FacilityMapper {

    // ENTITY → RESPONSE
    @Mapping(source = "businessDetail.businessDetailId", target = "businessDetailId")
    FacilityResponseDto toResponse(Facility entity);

    List<FacilityResponseDto> toResponseList(List<Facility> entities);

    // DTO → ENTITY
    @Mapping(target = "businessDetail", ignore = true)
    Facility toEntity(FacilityResponseDto dto);

    List<Facility> toEntityList(List<FacilityResponseDto> dtos);

    // PARTIAL UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "businessDetail", ignore = true)
    void updateEntity(@MappingTarget Facility entity, FacilityResponseDto dto);
}
