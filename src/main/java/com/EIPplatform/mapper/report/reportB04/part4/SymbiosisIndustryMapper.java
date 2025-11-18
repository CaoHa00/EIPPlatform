package com.EIPplatform.mapper.report.reportB04.part4;

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
            // Đảm bảo InvestorMapper đã được tạo và import đúng
            com.EIPplatform.mapper.report.reportB04.part3.ResourcesSavingAndReductionMapper.class,}
)
public interface SymbiosisIndustryMapper {

    //rsarId = auto increment
    //reportB04 have to be mapped manually
    @Mapping(target = "siId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    @Mapping(target = "symCompanies", ignore = true)
    SymbiosisIndustry dtoToEntity(SymbiosisIndustryDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "siId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    @Mapping(target = "symCompanies", ignore = true)
    void updateEntityFromDto(
            SymbiosisIndustryDTO request,
            @MappingTarget SymbiosisIndustry entity
    );

    @Mapping(target = "symCompanies", ignore = true)
    SymbiosisIndustryDTO toDTO(SymbiosisIndustry entity);
}
