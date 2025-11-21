package com.EIPplatform.service.report.reportB04.part1;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.LegalRepresentativeError;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.businessInformation.BusinessDetailMapper;
import com.EIPplatform.mapper.businessInformation.InvestorMapper;
import com.EIPplatform.mapper.businessInformation.LegalDocMapper;
import com.EIPplatform.mapper.businessInformation.ProjectMapper;
import com.EIPplatform.mapper.report.reportB04.part1.ReportInvestorDetailMapper;
import com.EIPplatform.mapper.report.reportB04.part1.ThirdPartyImplementerMapper;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorResponse;
import com.EIPplatform.model.dto.businessInformation.BusinessDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.investors.Investor;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheService;
import com.EIPplatform.util.StringNormalizerUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor // Tự động tiêm (inject) các trường 'final'
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportInvestorDetailServiceImpl implements ReportInvestorDetailService {

    ReportB04Repository reportB04Repository;
    ReportInvestorDetailMapper reportInvestorDetailMapper;
    ExceptionFactory exceptionFactory;
    BusinessDetailRepository businessDetailRepository;
    InvestorMapper investorMapper;
    LegalDocMapper legalDocMapper;
    ThirdPartyImplementerMapper thirdPartyImplementerMapper;    
    ProjectMapper projectMapper;
    BusinessDetailMapper businessDetailMapper;
    // Field khởi tạo trong @PostConstruct
    @NonFinal
    ReportCacheService<ReportB04DraftDTO> reportCacheService;

    @Override
    @Transactional
    public ReportInvestorDetailDTO createReportInvestorDetailDTO(UUID reportId, UUID userAccountId,
            ReportInvestorDetailCreateRequest request) {
        request = StringNormalizerUtil.normalizeRequest(request);

        ReportB04 report = reportB04Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                "ReportB04",
                "reportId",
                reportId,
                ReportError.REPORT_NOT_FOUND));

        ReportInvestorDetail entity = reportInvestorDetailMapper.toEntityFromCreate(request);

        ReportInvestorDetailDTO responseDto = reportInvestorDetailMapper.toDTO(entity);

        reportCacheService.updateSectionData(reportId, userAccountId, responseDto, "airEmissionData");

        log.info("Created AirEmissionData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true) // hàm này lấy tiếp dữ liệu từ cache report
    public ReportInvestorDetailDTO getReportInvestorDetailDTO(UUID reportId, UUID userAccountId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getReportInvestorDetailDTO() != null) {
            log.info("getReportInvestorDetailDTO- reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft.getReportInvestorDetailDTO();
        }

        log.warn("getReportInvestorDetailDTO not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return null;
    }

    @Override // hàm này fetch auto lên các dữ liệu chỗ business detail đã có
    public ReportInvestorDetailDTO getInitialReportInvestorDetailDTO(UUID reportId, UUID businessDetailId) {
        // part1: general detail - investor - third-party - projects
        BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                "BusinessDetail",
                "id",
                businessDetailId,
                LegalRepresentativeError.BUSINESS_DETAIL_NOT_FOUND));

        ReportInvestorDetailDTO dto = new ReportInvestorDetailDTO();
        dto.setEmail(businessDetail.getEmail());
        dto.setPhoneNumber(businessDetail.getPhoneNumber());
        dto.setFax(businessDetail.getFax());
        dto.setTaxCode(businessDetail.getTaxCode());

        Investor investorEntity = businessDetail.getInvestor();
        if (investorEntity != null) {
            InvestorResponse investorResponse = investorMapper.toResponse(investorEntity);
            dto.setInvestor(investorResponse);
        }

        // // Legal Documents (nếu có)
        // if (businessDetail.getLegalDoc() != null) {
        //     dto.setLegalDoc(legalDocMapper.toDto(businessDetail.getLegalDoc()));
        // }

        // // Third Party Implementer (nếu có)
        // if (businessDetail.getThirdPartyImplementer() != null) {
        //     dto.setThirdPartyImplementer(
        //             thirdPartyImplementerMapper.toDto(businessDetail.getThirdPartyImplementer()));
        // }

        // // Project (nếu có)
        // if (businessDetail.getProject() != null) {
        //     dto.setProject(projectMapper.toDto(businessDetail.getProject()));
        // }


        return dto;
       
    }

}
