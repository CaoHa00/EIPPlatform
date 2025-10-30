package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailResponse;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.EIPplatform.mapper.authentication.UserAccountMapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {
        UserAccountMapper.class
})
public interface BusinessDetailMapper {

    @Mapping(target = "userAccounts", source = "userAccounts")
    BusinessDetailResponse toResponse(BusinessDetail entity);

    List<BusinessDetailResponse> toResponseList(List<BusinessDetail> entities);

    @Mapping(source = "legalPresentative", target = "legalRepresentative")
    @Mapping(target = "businessDetailId", ignore = true)
    @Mapping(target = "businessHistoryConsumptions", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
//    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "envPermits", ignore = true)
    @Mapping(target = "envComponentPermits", ignore = true)
    @Mapping(target = "userAccounts", ignore = true)
    BusinessDetail toEntity(BusinessDetailDTO dto);

    List<BusinessDetail> toEntityList(List<BusinessDetailDTO> dtos);

    default String mapUserAccountToString(UserAccount userAccount) {
        if (userAccount == null) {
            return null;
        }
        return userAccount.getUserAccountId().toString();
    }

    default List<String> mapUserAccountsToStrings(List<UserAccount> userAccounts) {
        if (userAccounts == null) {
            return null;
        }
        return userAccounts.stream()
                .map(this::mapUserAccountToString)
                .collect(Collectors.toList());
    }
}