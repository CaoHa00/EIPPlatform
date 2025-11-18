package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeCreationNameOnly;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeCreationRequest;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeDTO;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeResponse;
import com.EIPplatform.model.dto.businessInformation.legalRepresentative.LegalRepresentativeUpdationRequest;
import com.EIPplatform.model.entity.businessInformation.legalRepresentative.LegalRepresentative;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LegalRepresentativeMapper {

    LegalRepresentative toEntity(LegalRepresentativeCreationRequest request);

    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "legalRepresentativeId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(LegalRepresentativeUpdationRequest request, @MappingTarget LegalRepresentative entity);

    LegalRepresentativeResponse toResponse(LegalRepresentative entity);

    LegalRepresentative nameToDraftEntity (LegalRepresentativeCreationNameOnly request);

    LegalRepresentativeDTO toDTO(LegalRepresentative entity);
}
