package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorOrganizationUpdateRequest;
import com.EIPplatform.model.entity.businessInformation.investors.InvestorOrganizationDetail;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
    LegalDocMapper.class})
public interface InvestorOrganizationMapper {

    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    InvestorOrganizationDetail toEntity(InvestorOrganizationCreationRequest request);

    @Mapping(target = "auditMetaData", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    void updateEntityFromRequest(InvestorOrganizationUpdateRequest request,
            @MappingTarget InvestorOrganizationDetail entity);

    @Mapping(target = "investorType", constant = "ORGANIZATION")
    InvestorOrganizationResponse toResponse(InvestorOrganizationDetail entity);
}
