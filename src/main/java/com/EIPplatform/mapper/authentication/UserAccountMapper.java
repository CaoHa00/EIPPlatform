package com.EIPplatform.mapper.authentication;

import org.mapstruct.Mapper;

import com.EIPplatform.model.dto.api.UserAccountDTO;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import java.util.List;
@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountDTO toDTO(UserAccount entity);
    UserAccount toEntity(UserAccountDTO dto);
    List<UserAccountDTO> toDTOList(List<UserAccount> entities);
    List<UserAccount> toEntityList(List<UserAccountDTO> dtos);
}
