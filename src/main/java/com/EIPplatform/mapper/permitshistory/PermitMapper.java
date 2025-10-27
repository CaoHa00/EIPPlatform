package com.EIPplatform.mapper.permitshistory;

import com.EIPplatform.model.dto.permitshistory.CreatePermitRequest;
import com.EIPplatform.model.dto.permitshistory.EnvPermitDTO;
import com.EIPplatform.model.dto.permitshistory.UpdatePermitRequest;
import com.EIPplatform.model.entity.permitsHistory.EnvPermits;
import com.EIPplatform.model.entity.user.userInformation.UserDetail;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PermitMapper {

    EnvPermitDTO toDTO(EnvPermits permit);

    @Mapping(target = "userDetail", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EnvPermits toEntity(CreatePermitRequest request, @Context UserDetail userDetail);

    @AfterMapping
    default void setUserDetailAndDefaults(@MappingTarget EnvPermits entity, @Context UserDetail userDetail) {
        entity.setUserDetail(userDetail);
        entity.setIsActive(true);
        entity.setCreatedAt(LocalDateTime.now());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UpdatePermitRequest request, @MappingTarget EnvPermits permit);

    @AfterMapping
    default void calculateDaysUntilExpiry(@MappingTarget EnvPermitDTO dto, EnvPermits permit) {
        if (permit.getIssueDate() != null) {
            LocalDate expiryDate = permit.getIssueDate().plusYears(5);
            long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
            dto.setDaysUntilExpiry((int) daysUntil);
        }
    }

    List<EnvPermitDTO> toDTOList(List<EnvPermits> permits);

    @Mapping(target = "userDetail", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    List<EnvPermits> toEntityList(List<CreatePermitRequest> requests);
}