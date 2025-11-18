package com.EIPplatform.mapper.authentication;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.EIPplatform.model.dto.userAccount.*;
import com.EIPplatform.model.entity.user.authentication.UserAccount;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAccountMapper {

    // CREATE DTO -> Entity
    UserAccount toEntity(UserAccountCreateDTO dto);

    // UPDATE DTO -> Entity
    void updateFromDTO(UserAccountUpdateDTO dto, @MappingTarget UserAccount entity);

    // Entity -> ResponseDTO
    UserAccountResponseDTO toResponseDTO(UserAccount entity);
}
