package com.EIPplatform.mapper.report.reportB04.part1;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // <-- THÊM IMPORT NÀY
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.EIPplatform.mapper.businessInformation.InvestorMapper;
import com.EIPplatform.mapper.businessInformation.LegalDocMapper;
import com.EIPplatform.mapper.businessInformation.ProjectMapper;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ReportInvestorDetailResponse;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;

@Mapper(componentModel = "spring", uses = {
                // Đảm bảo InvestorMapper đã được tạo và import đúng
                InvestorMapper.class,
               LegalDocMapper.class,
                ThirdPartyImplementerMapper.class,ProjectMapper.class
})
public interface ReportInvestorDetailMapper {

        @Mapping(target = "ridId", ignore = true)
        @Mapping(target = "investor", ignore = true)
        @Mapping(target = "legalDoc", ignore = true)
        @Mapping(target = "thirdPartyImplementer", ignore = true)
        @Mapping(target = "project", ignore = true)
        ReportInvestorDetail toEntityFromCreate(ReportInvestorDetailCreateRequest request);

        @Mapping(target = "ridId", ignore = true)
        @Mapping(target = "investor", ignore = true)
        @Mapping(target = "legalDoc", ignore = true)
        @Mapping(target = "thirdPartyImplementer", ignore = true)
        @Mapping(target = "project", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateEntityFromUpdate(
                        ReportInvestorDetailUpdateRequest request,
                        @MappingTarget ReportInvestorDetail entity);

        @Mapping(target = "investor", ignore = true)
        ReportInvestorDetailResponse toResponse(ReportInvestorDetail entity);

        @Mapping(target = "ridId", ignore = true)
        @Mapping(target = "thirdPartyImplementer", source = "thirdPartyImplementer", qualifiedByName = "toThirdPartyDTO")
        @Mapping(target = "project", source = "project", qualifiedByName = "toProjectDTO")
        @Mapping(target = "investor", source = "investor", qualifiedByName = "toInvestorResponse")
        ReportInvestorDetailDTO toDTO(ReportInvestorDetail entity);

        @Mapping(target = "ridId", ignore = true)
        @Mapping(target = "reportB04", ignore = true)
        @Mapping(target = "investor", ignore = true)
        @Mapping(target = "legalDoc", ignore = true)
        @Mapping(target = "thirdPartyImplementer", ignore = true)
        ReportInvestorDetail dtoToEntity(ReportInvestorDetailDTO dto);

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        @Mapping(target = "ridId", ignore = true)
        @Mapping(target = "reportB04", ignore = true)
        @Mapping(target = "investor", ignore = true)
        @Mapping(target = "legalDoc", ignore = true)
        @Mapping(target = "thirdPartyImplementer", ignore = true)
        void updateEntityFromDto(ReportInvestorDetailDTO dto, @MappingTarget ReportInvestorDetail entity);

        @Mapping(target = "investor", ignore = true)
        List<ReportInvestorDetailResponse> toResponseList(List<ReportInvestorDetail> entities);

}
