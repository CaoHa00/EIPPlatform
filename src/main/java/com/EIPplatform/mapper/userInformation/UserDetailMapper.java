package com.EIPplatform.mapper.userInformation;

import org.mapstruct.Mapper;

import com.EIPplatform.mapper.authentication.UserAccountMapper;
import com.EIPplatform.model.dto.userInformation.UserDetailDTO;
import com.EIPplatform.model.entity.user.userInformation.BusinessDetail;

@Mapper(componentModel = "spring", uses = {
        UserAccountMapper.class,
        UserHistoryConsumptionMapper.class
})
public interface UserDetailMapper {
    UserDetailDTO toDTO(BusinessDetail entity);
    BusinessDetail toEntity(UserDetailDTO dto);
    java.util.List<UserDetailDTO> toDTOList(java.util.List<BusinessDetail> entities);
    java.util.List<BusinessDetail> toEntityList(java.util.List<UserDetailDTO> dtos);

}
