package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.*;

import com.EIPplatform.model.dto.legalRepresentative.LegalRepresentativeCreationRequest;
import com.EIPplatform.model.dto.legalRepresentative.LegalRepresentativeDTO;
import com.EIPplatform.model.dto.legalRepresentative.LegalRepresentativeResponse;
import com.EIPplatform.model.dto.legalRepresentative.LegalRepresentativeUpdationRequest;
import com.EIPplatform.model.entity.user.legalRepresentative.LegalRepresentative;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LegalRepresentativeMapper {

    LegalRepresentative toEntity(LegalRepresentativeCreationRequest request);

    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "legalRepresentativeId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(LegalRepresentativeUpdationRequest request, @MappingTarget LegalRepresentative entity);

    LegalRepresentativeResponse toResponse(LegalRepresentative entity);

    LegalRepresentativeDTO toDTO(LegalRepresentative entity);
}
