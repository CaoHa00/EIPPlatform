package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.*;

import com.EIPplatform.model.dto.investors.InvestorOrganizationCreationRequest;
import com.EIPplatform.model.dto.investors.InvestorOrganizationResponse;
import com.EIPplatform.model.dto.investors.InvestorOrganizationUpdateRequest;
import com.EIPplatform.model.entity.user.investors.InvestorOrganizationDetail;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
        LegalDocMapper.class })
public interface InvestorOrganizationMapper {

    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "legalDocs", ignore = true) // vẫn ignore để add thủ công
    @Mapping(target = "auditMetaData", ignore = true)
    InvestorOrganizationDetail toEntity(InvestorOrganizationCreationRequest request);

    @Mapping(target = "legalDocs", ignore = true) // tránh ghi đè list hiện có
    @Mapping(target = "auditMetaData", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(InvestorOrganizationUpdateRequest request,
            @MappingTarget InvestorOrganizationDetail entity);

    InvestorOrganizationResponse toResponse(InvestorOrganizationDetail entity);
}