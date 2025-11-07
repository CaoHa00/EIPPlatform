package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionCreateDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionUpdateDTO;
import com.EIPplatform.model.entity.user.businessInformation.BusinessHistoryConsumption;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BusinessHistoryConsumptionMapper {

    @Mapping(source = "businessDetail.businessDetailId", target = "businessDetailId")
    @Mapping(source = "auditMetaData.createdAt", target = "createdAt")
    @Mapping(source = "auditMetaData.createdBy", target = "createdBy")
    @Mapping(source = "auditMetaData.updatedAt", target = "updatedAt")
    @Mapping(source = "auditMetaData.updatedBy", target = "updatedBy")
    BusinessHistoryConsumptionDTO toDTO(BusinessHistoryConsumption entity);

    List<BusinessHistoryConsumptionDTO> toDTOList(List<BusinessHistoryConsumption> entities);

    @Mapping(target = "businessHistoryConsumptionId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    BusinessHistoryConsumption toEntity(BusinessHistoryConsumptionCreateDTO dto);

    List<BusinessHistoryConsumption> toEntityList(List<BusinessHistoryConsumptionCreateDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "businessHistoryConsumptionId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    @Mapping(target = "auditMetaData", ignore = true)
    void updateEntityFromDto(BusinessHistoryConsumptionUpdateDTO dto, @MappingTarget BusinessHistoryConsumption entity);
}