package com.EIPplatform.mapper.businessInformation;
import com.EIPplatform.model.entity.businessInformation.Process;

import com.EIPplatform.model.dto.businessInformation.process.ProcessResponseDto;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcessMapper {

    // ENTITY → RESPONSE
    @Mapping(source = "businessDetail.businessDetailId", target = "businessDetailId")
    ProcessResponseDto toResponse(Process entity);

    List<ProcessResponseDto> toResponseList(List<Process> entities);

    // DTO → ENTITY
    @Mapping(target = "businessDetail", ignore = true)
    Process toEntity(ProcessResponseDto dto);

    List<Process> toEntityList(List<ProcessResponseDto> dtos);

    // PARTIAL UPDATE
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "businessDetail", ignore = true)
//    @Mapping(target = "id", ignore = true) // thêm dòng này để an toàn tuyệt đối
    void updateFromDto(@MappingTarget Process entity, ProcessResponseDto dto);

}
