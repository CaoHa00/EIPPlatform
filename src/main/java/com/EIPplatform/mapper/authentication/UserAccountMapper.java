package com.EIPplatform.mapper.authentication;

import org.mapstruct.Mapper;

import com.EIPplatform.model.dto.api.UserAccountDTO;
import com.EIPplatform.model.entity.user.authentication.UserAccount;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountDTO toDTO(UserAccount entity);
    UserAccount toEntity(UserAccountDTO dto);
    java.util.List<UserAccountDTO> toDTOList(java.util.List<UserAccount> entities);
    java.util.List<UserAccount> toEntityList(java.util.List<UserAccountDTO> dtos);
}
