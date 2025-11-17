package com.EIPplatform.mapper.report.reportB04.part3;

import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionCreateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionUpdateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.response.ResourcesSavingAndReductionReponseDTO;
import com.EIPplatform.model.entity.report.reportB04.part03.ResourcesSavingAndReduction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {
            // Đảm bảo InvestorMapper đã được tạo và import đúng
            com.EIPplatform.mapper.report.reportB04.part3.ResourcesSavingAndReductionMapper.class,}
)
public interface ResourcesSavingAndReductionMapper {

    //rsarId = auto increment
    //reportB04 have to be mapped manually
    @Mapping(target = "rsarId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    ResourcesSavingAndReduction toEntityFromCreate(ResourcesSavingAndReductionCreateRequestDTO request);

    //rsarId = auto increment
    //reportB04 have to be mapped manually
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rsarId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    void updateEntityFromUpdate(
            ResourcesSavingAndReductionUpdateRequestDTO request,
            @MappingTarget ResourcesSavingAndReduction entity
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rsarId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    void updateEntityFromDto(
            ResourcesSavingAndReductionDTO request,
            @MappingTarget ResourcesSavingAndReduction entity
    );

    @Mapping(target = "rsarId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    ResourcesSavingAndReduction dtoToEntity(ResourcesSavingAndReductionDTO dto);

    @Mapping(target = "rsarId", ignore = true)
    ResourcesSavingAndReductionDTO toDTO(ResourcesSavingAndReduction entity);

    @Mapping(target = "reportId", ignore = true)
    ResourcesSavingAndReductionReponseDTO toResponse(ResourcesSavingAndReduction entity);

}
