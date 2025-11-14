package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BusinessDetailMapper {


    @Mapping(target = "legalRepresentative", source = "legalRepresentative.legalRepresentativeId")
    @Mapping(target = "scaleCapacity", source = "scaleCapacity.scaleCapacityId", ignore = true) // not in response yet
    @Mapping(target = "userAccounts", expression = "java(mapUserAccounts(entity.getUserAccounts()))")
    @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt())")
    @Mapping(target = "createdBy", expression = "java(entity.getCreatedBy())")
    @Mapping(target = "updatedAt", expression = "java(entity.getUpdatedAt())")
    @Mapping(target = "updatedBy", expression = "java(entity.getUpdatedBy())")
    BusinessDetailResponse toResponse(BusinessDetail entity);

    List<BusinessDetailResponse> toResponseList(List<BusinessDetail> entities);


    @Mapping(target = "businessDetailId", ignore = true)
    @Mapping(target = "legalRepresentative", ignore = true)  // will be set in service
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "envPermits", ignore = true)
    @Mapping(target = "envComponentPermits", ignore = true)
    @Mapping(target = "businessHistoryConsumptions", ignore = true)
    @Mapping(target = "userAccounts", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    BusinessDetail toEntity(BusinessDetailDTO dto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "legalRepresentative", ignore = true)
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "scaleCapacity", ignore = true)
    @Mapping(target = "projects", ignore = true)
    @Mapping(target = "facilities", ignore = true)
    @Mapping(target = "equipments", ignore = true)
    @Mapping(target = "processes", ignore = true)
    @Mapping(target = "userAccounts", ignore = true)
    @Mapping(target = "businessHistoryConsumptions", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    void updateEntity(@MappingTarget BusinessDetail entity, BusinessDetailDTO dto);

    default List<String> mapUserAccounts(List<UserAccount> accounts) {
        if (accounts == null) return null;
        return accounts.stream()
                .map(a -> a.getUserAccountId().toString())
                .collect(Collectors.toList());
    }
}
