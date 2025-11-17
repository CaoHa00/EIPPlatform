
package com.EIPplatform.mapper.businessInformation.permitshistory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.businessInformation.permitshistory.CreateComponentPermitRequest;
import com.EIPplatform.model.dto.businessInformation.permitshistory.CreateMainPermitRequest;
import com.EIPplatform.model.dto.businessInformation.permitshistory.EnvComponentPermitDTO;
import com.EIPplatform.model.dto.businessInformation.permitshistory.EnvPermitDTO;
import com.EIPplatform.model.dto.businessInformation.permitshistory.UpdateComponentPermitRequest;
import com.EIPplatform.model.dto.businessInformation.permitshistory.UpdateEnvPermitRequest;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.permitshistory.EnvComponentPermit;
import com.EIPplatform.model.entity.businessInformation.permitshistory.EnvPermits;

@Mapper(componentModel = "spring")
public interface PermitMapper {

    // ==================== MAIN PERMIT MAPPINGS ====================

    @Mapping(target = "daysUntilExpiry", ignore = true)
    EnvPermitDTO toMainPermitDTO(EnvPermits permit);

    @AfterMapping
    default void calculateMainPermitDaysUntilExpiry(@MappingTarget EnvPermitDTO dto, EnvPermits permit) {
        if (permit.getIssueDate() != null) {
            LocalDate expiryDate = permit.getIssueDate().plusYears(5);
            long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
            dto.setDaysUntilExpiry((int) daysUntil);
        }
    }

    @Mapping(target = "permitId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "permitFilePath", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EnvPermits toMainPermitEntity(CreateMainPermitRequest request, @Context BusinessDetail businessDetail);

    @AfterMapping
    default void setMainPermitBusinessDetailAndDefaults(@MappingTarget EnvPermits entity,
            @Context BusinessDetail businessDetail) {
        entity.setBusinessDetail(businessDetail);
        entity.setIsActive(true);
        entity.setCreatedAt(LocalDateTime.now());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "permitId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "permitFilePath", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateMainPermitFromDTO(UpdateEnvPermitRequest request, @MappingTarget EnvPermits permit);

    List<EnvPermitDTO> toMainPermitDTOList(List<EnvPermits> permits);

    // ==================== COMPONENT PERMIT MAPPINGS ====================

    @Mapping(target = "daysUntilExpiry", ignore = true)
    EnvComponentPermitDTO toComponentPermitDTO(EnvComponentPermit permit);

    @AfterMapping
    default void calculateComponentPermitDaysUntilExpiry(@MappingTarget EnvComponentPermitDTO dto,
            EnvComponentPermit permit) {
        if (permit.getIssueDate() != null) {
            LocalDate expiryDate = permit.getIssueDate().plusYears(5);
            long daysUntil = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
            dto.setDaysUntilExpiry((int) daysUntil);
        }
    }

    @Mapping(target = "permitId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "permitFilePath", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    EnvComponentPermit toComponentPermitEntity(CreateComponentPermitRequest request,
            @Context BusinessDetail businessDetail);

    @AfterMapping
    default void setComponentPermitBusinessDetailAndDefaults(@MappingTarget EnvComponentPermit entity,
            @Context BusinessDetail businessDetail) {
        entity.setBusinessDetail(businessDetail);
        entity.setIsActive(true);
        entity.setCreatedAt(LocalDateTime.now());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "permitId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "permitFilePath", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateComponentPermitFromDTO(UpdateComponentPermitRequest request, @MappingTarget EnvComponentPermit permit);

    List<EnvComponentPermitDTO> toComponentPermitDTOList(List<EnvComponentPermit> permits);
}