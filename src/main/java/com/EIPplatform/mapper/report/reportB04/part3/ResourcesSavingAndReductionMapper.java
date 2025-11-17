package com.EIPplatform.mapper.report.reportB04.part3;

import com.EIPplatform.model.dto.report.reportB04.part1.response.ReportInvestorDetailResponse;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionCreateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionUpdateRequestDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.response.ResourcesSavingAndReductionReponseDTO;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import com.EIPplatform.model.entity.report.reportB04.part03.ResourcesSavingAndReduction;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
            // Đảm bảo InvestorMapper đã được tạo và import đúng
            com.EIPplatform.mapper.report.reportB04.part3.ResourcesSavingAndReductionMapper.class,
        }
)
public interface ResourcesSavingAndReductionMapper {


    //rsarId = auto increment
    //reportB04 have to be mapped manually
    @Mapping(target = "rsarId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    ResourcesSavingAndReduction toEntityFromCreate(ResourcesSavingAndReductionCreateRequestDTO request);

    //rsarId = auto increment
    //reportB04 have to be mapped manually
    @Mapping(target = "rsarId", ignore = true)
    @Mapping(target = "reportB04", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(
            ResourcesSavingAndReductionUpdateRequestDTO request,
            @MappingTarget ResourcesSavingAndReduction entity
    );

    //reportB04 have to be mapped manually
    @Mapping(target = "reportId", ignore = true)
    ResourcesSavingAndReductionReponseDTO toResponse(ResourcesSavingAndReduction entity);

    List<ReportInvestorDetailResponse> toResponseList(List<ReportInvestorDetail> entities);

}
