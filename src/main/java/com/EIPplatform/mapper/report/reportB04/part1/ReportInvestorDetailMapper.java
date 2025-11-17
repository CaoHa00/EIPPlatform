package com.EIPplatform.mapper.report.reportB04.part1;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping; // <-- THÊM IMPORT NÀY
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailUpdateRequest;
import com.EIPplatform.model.dto.report.reportB04.part1.response.ReportInvestorDetailResponse;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;

@Mapper(
        componentModel = "spring",
        uses = {
            // Đảm bảo InvestorMapper đã được tạo và import đúng
            com.EIPplatform.mapper.businessInformation.InvestorMapper.class,
            com.EIPplatform.mapper.businessInformation.LegalDocMapper.class,
            com.EIPplatform.mapper.report.reportB04.part1.ThirdPartyImplementerMapper.class
        }
)
public interface ReportInvestorDetailMapper {

    /**
     * 🏗️ CreateRequest → Entity * Giải quyết lỗi "Unmapped target property:
     * ridId" và "Can't map property... Investor". - ridId là auto-generated,
     * phải ignore. - investor là abstract và cần logic nghiệp vụ (lấy từ DB),
     * vì vậy phải ignore và set thủ công trong Service.
     */
    @Mapping(target = "ridId", ignore = true)
    @Mapping(target = "investor", ignore = true)
    ReportInvestorDetail toEntityFromCreate(ReportInvestorDetailCreateRequest request);

    /**
     * 🧩 UpdateRequest → Entity (chỉ ghi đè field không null) * Tương tự, không
     * bao giờ map ID và investor từ request.
     */
    @Mapping(target = "ridId", ignore = true)
    @Mapping(target = "investor", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdate(
            ReportInvestorDetailUpdateRequest request,
            @MappingTarget ReportInvestorDetail entity
    );

    @Mapping(source = "investor", target = "investor", qualifiedByName = "toInvestorResponse")
    ReportInvestorDetailResponse toResponse(ReportInvestorDetail entity);

    @Mapping(target = "ridId", ignore = true)
    @Mapping(target = "investor", ignore = true)
    @Mapping(target = "legalDoc", ignore = true) 
    @Mapping(target = "thirdPartyImplementer", ignore = true)
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

    List<ReportInvestorDetailResponse> toResponseList(List<ReportInvestorDetail> entities);

}
