package com.EIPplatform.mapper.report.reportB04.part4;


import com.EIPplatform.model.dto.report.reportB04.part4.request.SymCompanyCreateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.request.SymCompanyUpdateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.response.SymCompanyResponseDTO;
import com.EIPplatform.model.entity.report.reportB04.part04.SymCompany;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {
                // Đảm bảo InvestorMapper đã được tạo và import đúng
                SymCompanyMapper.class,
        }
)
public interface SymCompanyMapper {


    //scId = auto increment
    //symbiosisIndustry have to be mapped manually
    @Mapping(target = "scId", ignore = true)
    @Mapping(target = "symbiosisIndustry", ignore = true)
    SymCompany toEntityFromCreate(SymCompanyCreateRequestDTO request);

    //scId = auto increment
    //symbiosisIndustry have to be mapped manually
    @Mapping(target = "scId", ignore = true)
    @Mapping(target = "symbiosisIndustry", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(
            SymCompanyUpdateRequestDTO request,
            @MappingTarget SymCompany entity
    );

    //symbiosisIndustryId have to be mapped manually
    @Mapping(target = "symbiosisIndustryId", ignore = true)
    SymCompanyResponseDTO toResponse(SymCompany entity);

}