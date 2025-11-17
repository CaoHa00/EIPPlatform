package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.equipment.EquipmentResponseDto;
import com.EIPplatform.model.entity.user.businessInformation.Equipment;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    // ENTITY → RESPONSE
    @Mapping(source = "businessDetail.businessDetailId", target = "businessId")
    EquipmentResponseDto toResponse(Equipment entity);

    List<EquipmentResponseDto> toResponseList(List<Equipment> entities);

    // DTO → ENTITY
    @Mapping(target = "equipmentId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    Equipment toEntity(EquipmentResponseDto dto);

    List<Equipment> toEntityList(List<EquipmentResponseDto> dtos);

    // PARTIAL UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "businessDetail", ignore = true)
    void updateEntity(@MappingTarget Equipment entity, EquipmentResponseDto dto);
}
