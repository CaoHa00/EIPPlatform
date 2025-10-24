package com.EIPplatform.mapper.userInformation;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.userInformation.UserHistoryConsumptionDTO;
import com.EIPplatform.model.entity.user.userInformation.UserHistoryConsumption;

@Mapper(componentModel = "spring")
public interface  UserHistoryConsumptionMapper {

    // Entity to DTO
    UserHistoryConsumptionDTO toDTO(UserHistoryConsumption entity);

    // DTO to Entity
    UserHistoryConsumption toEntity(UserHistoryConsumptionDTO dto);

    // List mapping
    java.util.List<UserHistoryConsumptionDTO> toDTOList(java.util.List<UserHistoryConsumption> entities);

    java.util.List<UserHistoryConsumption> toEntityList(java.util.List<UserHistoryConsumptionDTO> dtos);
     @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserHistoryConsumptionDTO dto, @MappingTarget UserHistoryConsumption entity);
}
