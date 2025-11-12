package com.EIPplatform.mapper.businessInformation;


import org.mapstruct.*;

import com.EIPplatform.model.dto.legalDocs.LegalDocsCreationRequest;
import com.EIPplatform.model.dto.legalDocs.LegalDocsResponse;
import com.EIPplatform.model.dto.legalDocs.LegalDocsUpdateRequest;
import com.EIPplatform.model.entity.user.legalDocs.LegalDocs;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LegalDocsMapper {

    @Mapping(target = "legalDocId", ignore = true)
    @Mapping(target = "investorOrganization", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    LegalDocs toEntity(LegalDocsCreationRequest request);

    @Mapping(target = "investorOrganization", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(LegalDocsUpdateRequest request, @MappingTarget LegalDocs entity);

    @Mapping(source = "investorOrganization.investorId", target = "investorOrganizationId")
    @Mapping(source = "investorOrganization.organizationName", target = "investorOrganizationName")
    LegalDocsResponse toResponse(LegalDocs entity);
}