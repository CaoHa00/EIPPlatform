package com.EIPplatform.mapper.businessInformation;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.mapper.authentication.UserAccountMapper;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.legalRepresentative.LegalRepresentative;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.mapstruct.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BusinessDetailMapper {

    // @Mapping(target = "userAccounts", source = "userAccounts")
    @Mapping(source = "ISO_certificate_14001", target = "ISO_certificate_14001")
    @Mapping(source = "isoCertificateFilePath", target = "isoCertificateFilePath")
    @Mapping(target = "businessDetailId", source = "businessDetailId")
    @Mapping(target = "facilityName", source = "facilityName")
    @Mapping(target = "isoCertificateFilePath", source = "isoCertificateFilePath")
    @Mapping(target = "legalRepresentative", source = "legalRepresentative")
    @Mapping(target = "scaleCapacity", source = "scaleCapacity.totalArea")
    @Mapping(target = "userAccounts", expression = "java(mapUserAccounts(entity.getUserAccounts()))")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "updatedBy", source = "updatedBy")
    BusinessDetailResponse toResponse(BusinessDetail entity);

    default List<BusinessDetailResponse> toResponseList(List<BusinessDetail> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toResponse).toList();
    }

    @Mapping(target = "legalRepresentative", ignore = true)
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "businessDetailId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "userAccounts", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "envPermits", ignore = true)
    @Mapping(target = "envComponentPermits", ignore = true)
    @Mapping(target = "userAccounts", ignore = true)
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "legalRepresentative", ignore = true)
    @Mapping(source = "ISO_certificate_14001", target = "ISO_certificate_14001")
    @Mapping(target = "isoCertificateFilePath", ignore = true)
    @Mapping(target = "businessHistoryConsumptions", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "products", ignore = true)
    BusinessDetail toEntity(BusinessDetailDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "legalRepresentative", ignore = true)
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "businessDetailId", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "userAccounts", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "envPermits", ignore = true)
    @Mapping(target = "envComponentPermits", ignore = true)
    @Mapping(target = "businessHistoryConsumptions", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateEntity(@MappingTarget BusinessDetail entity, BusinessDetailDTO dto);

    default List<String> mapUserAccounts(List<UserAccount> accounts) {
        if (accounts == null) return null;
        return accounts.stream()
                .map(a -> a.getUserAccountId().toString())
                .collect(Collectors.toList());
    }

    default LegalRepresentative uuidToLegalRepresentative(UUID id) {
        if (id == null) return null;
        LegalRepresentative rep = new LegalRepresentative();
        rep.setLegalRepresentativeId(id);
        return rep;
    }
}