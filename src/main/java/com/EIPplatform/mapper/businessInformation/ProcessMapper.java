package com.EIPplatform.mapper.businessInformation;

import com.EIPplatform.model.dto.businessInformation.process.ProcessResponseDto;
import com.EIPplatform.model.entity.user.businessInformation.Process;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcessMapper {

    // ENTITY → RESPONSE
    @Mapping(source = "businessDetail.businessDetailId", target = "businessId")
    ProcessResponseDto toResponse(Process entity);

    List<ProcessResponseDto> toResponseList(List<Process> entities);

    // DTO → ENTITY
    @Mapping(target = "processId", ignore = true)
    @Mapping(target = "businessDetail", ignore = true)
    Process toEntity(ProcessResponseDto dto);

    List<Process> toEntityList(List<ProcessResponseDto> dtos);

    // PARTIAL UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "businessDetail", ignore = true)
    void updateEntity(@MappingTarget Process entity, ProcessResponseDto dto);
}
