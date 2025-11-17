
package com.EIPplatform.mapper.businessInformation;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.model.dto.businessInformation.BusinessHistoryConsumptionDTO;
import com.EIPplatform.model.entity.businessInformation.BusinessHistoryConsumption;

@Mapper(componentModel = "spring")
public interface BusinessDetailHistoryConsumptionMapper {

    BusinessHistoryConsumptionDTO toDTO(BusinessHistoryConsumption entity);

    // DTO to Entity
    BusinessHistoryConsumption toEntity(BusinessHistoryConsumptionDTO dto);

    // List mapping
    java.util.List<BusinessHistoryConsumptionDTO> toDTOList(java.util.List<BusinessHistoryConsumption> entities);

    java.util.List<BusinessHistoryConsumption> toEntityList(java.util.List<BusinessHistoryConsumptionDTO> dtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(BusinessHistoryConsumptionDTO dto, @MappingTarget BusinessHistoryConsumption entity);
}
