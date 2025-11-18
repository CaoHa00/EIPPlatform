
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

@Mapper(componentModel = "spring", uses = {
        UserAccountMapper.class
})
public interface BusinessDetailMapper {

    // @Mapping(target = "userAccounts", source = "userAccounts")
    @Mapping(source = "ISO_certificate_14001", target = "ISO_certificate_14001")
    @Mapping(source = "isoCertificateFilePath", target = "isoCertificateFilePath")
    BusinessDetailResponse toResponse(BusinessDetail entity);

    List<BusinessDetailResponse> toResponseList(List<BusinessDetail> entities);

    @Mapping(target = "businessDetailId", ignore = true)
    @Mapping(target = "businessHistoryConsumptions", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    @Mapping(target = "reports", ignore = true)
    @Mapping(target = "envPermits", ignore = true)
    @Mapping(target = "envComponentPermits", ignore = true)
    @Mapping(target = "userAccounts", ignore = true)
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "legalRepresentative", ignore = true)
    @Mapping(source = "ISO_certificate_14001", target = "ISO_certificate_14001")
    @Mapping(target = "isoCertificateFilePath", ignore = true)
    @Mapping(target = "products", ignore = true)
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

    default String map(LegalRepresentative legalRepresentative) {
        if (legalRepresentative == null) {
            return null;
        }
        return legalRepresentative.getLegalRepresentativeId().toString();
    }

}