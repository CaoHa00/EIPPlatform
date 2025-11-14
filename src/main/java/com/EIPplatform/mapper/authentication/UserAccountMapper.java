package com.EIPplatform.mapper.authentication;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.EIPplatform.model.dto.api.UserAccountDTO;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAccountMapper {
    UserAccountDTO toDTO(UserAccount entity);
    UserAccount toEntity(UserAccountDTO dto);
    List<UserAccountDTO> toDTOList(List<UserAccount> entities);
    List<UserAccount> toEntityList(List<UserAccountDTO> dtos);
}
