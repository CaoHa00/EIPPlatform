package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.*;

import com.EIPplatform.model.dto.legalDoc.LegalDocCreationRequest;
import com.EIPplatform.model.dto.legalDoc.LegalDocResponse;
import com.EIPplatform.model.dto.legalDoc.LegalDocUpdateRequest;
import com.EIPplatform.model.entity.user.legalDoc.LegalDoc;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LegalDocMapper {

    @Mapping(target = "legalDocId", ignore = true)
    @Mapping(target = "investorOrganization", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    LegalDoc toEntity(LegalDocCreationRequest request);

    @Mapping(target = "investorOrganization", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(LegalDocUpdateRequest request, @MappingTarget LegalDoc entity);

    @Mapping(source = "investorOrganization.investorId", target = "investorOrganizationId")
    @Mapping(source = "investorOrganization.organizationName", target = "investorOrganizationName")
    LegalDocResponse toResponse(LegalDoc entity);
}