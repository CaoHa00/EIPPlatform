package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.*;

import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualCreationRequest;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualResponse;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorIndividualUpdateRequest;
import com.EIPplatform.model.entity.businessInformation.investors.InvestorIndividualDetail;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvestorIndividualMapper {

    @Mapping(target = "auditMetaData", ignore = true)
        @Mapping(target = "investorId", ignore = true)
            @Mapping(target = "businessDetail", ignore = true)
    InvestorIndividualDetail toEntity(InvestorIndividualCreationRequest request);

    @Mapping(target = "investorId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    void updateEntityFromRequest(InvestorIndividualUpdateRequest request,
            @MappingTarget InvestorIndividualDetail entity);

    @Mapping(target = "investorType", constant = "INDIVIDUAL")
    InvestorIndividualResponse toResponse(InvestorIndividualDetail entity);
}
