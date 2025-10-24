package com.EIPplatform.mapper.userInformation;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.EIPplatform.model.dto.userInformation.UserDetailWithHistoryConsumptionDTO;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;
@Mapper(
    componentModel = "spring",
    uses = { UserDetailMapper.class, UserHistoryConsumptionMapper.class }
)
public interface UserDetailWithHistoryConsumptionMapper {
    @Mapping(target = "userDetail", source = "entity")
    UserDetailWithHistoryConsumptionDTO toDTO(UserDetail entity);

    List<UserDetailWithHistoryConsumptionDTO> toDTOList(List<UserDetail> entities);
}
