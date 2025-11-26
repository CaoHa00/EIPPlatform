package com.EIPplatform.mapper.report.reportB04.part4;

import com.EIPplatform.model.dto.report.reportB04.part4.request.SymbiosisIndustryCreateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.request.SymbiosisIndustryUpdateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.response.SymbiosisIndustryReponseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.report.reportB04.part4.SymbiosisIndustryDTO;
import com.EIPplatform.model.entity.report.reportB04.part04.SymbiosisIndustry;

@Mapper(
        componentModel = "spring",
        uses = {
            com.EIPplatform.mapper.report.reportB04.part4.SymbiosisIndustryMapper.class,}
)
public interface SymbiosisIndustryMapper {

    //siId = auto increment
    //reportB04 have to be mapped manually
    @Mapping(target = "siId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    SymbiosisIndustry toEntityFromCreate(SymbiosisIndustryCreateRequestDTO request);

    //siId = auto increment
    //reportB04 have to be mapped manually
    @Mapping(target = "siId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(
            SymbiosisIndustryUpdateRequestDTO request,
            @MappingTarget SymbiosisIndustry entity
    );

    //symbiosisIndustryId have to be mapped manually
    @Mapping(target = "reportB04Id", ignore = true)
    SymbiosisIndustryReponseDTO toResponse(SymbiosisIndustry entity);

    //rsarId = auto increment
    //reportB04 have to be mapped manually
    @Mapping(target = "siId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    SymbiosisIndustry dtoToEntity(SymbiosisIndustryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "siId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    void updateEntityFromDto(
            SymbiosisIndustryDTO request,
            @MappingTarget SymbiosisIndustry entity
    );

//    @Mapping(target = "symCompanies", ignore = true)
    SymbiosisIndustryDTO toDTO(SymbiosisIndustry entity);
}
