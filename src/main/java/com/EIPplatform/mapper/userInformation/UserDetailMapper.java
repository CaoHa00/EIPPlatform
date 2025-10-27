package com.EIPplatform.mapper.userInformation;

import org.mapstruct.Mapper;

import com.EIPplatform.mapper.authentication.UserAccountMapper;
import com.EIPplatform.model.dto.userInformation.UserDetailDTO;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;

@Mapper(componentModel = "spring", uses = {
        UserAccountMapper.class,
        UserHistoryConsumptionMapper.class
})
public interface UserDetailMapper {
    UserDetailDTO toDTO(UserDetail entity);
    UserDetail toEntity(UserDetailDTO dto);
    java.util.List<UserDetailDTO> toDTOList(java.util.List<UserDetail> entities);
    java.util.List<UserDetail> toEntityList(java.util.List<UserDetailDTO> dtos);

}
