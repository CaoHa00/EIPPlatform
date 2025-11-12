package com.EIPplatform.mapper.businessInformation;


import org.mapstruct.*;

import com.EIPplatform.model.dto.investors.InvestorIndividualCreationRequest;
import com.EIPplatform.model.dto.investors.InvestorIndividualResponse;
import com.EIPplatform.model.dto.investors.InvestorIndividualUpdateRequest;
import com.EIPplatform.model.entity.user.investors.InvestorIndividualDetail;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvestorIndividualMapper {

    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "investorType", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    InvestorIndividualDetail toEntity(InvestorIndividualCreationRequest request);

    @Mapping(target = "investorType", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(InvestorIndividualUpdateRequest request, @MappingTarget InvestorIndividualDetail entity);

    @Mapping(source = "auditMetaData.createdAt", target = "createdAt")
    @Mapping(source = "auditMetaData.createdBy", target = "createdBy")
    @Mapping(source = "auditMetaData.updatedAt", target = "updatedAt")
    @Mapping(source = "auditMetaData.updatedBy", target = "updatedBy")
    InvestorIndividualResponse toResponse(InvestorIndividualDetail entity);
}