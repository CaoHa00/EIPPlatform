package com.EIPplatform.service.report.reportB04.part1;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.LegalRepresentativeError;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.businessInformation.InvestorMapper;
import com.EIPplatform.mapper.businessInformation.ProjectMapper;
import com.EIPplatform.mapper.report.reportB04.part1.ReportInvestorDetailMapper;
import com.EIPplatform.mapper.report.reportB04.part1.ThirdPartyImplementerMapper;
import com.EIPplatform.model.dto.businessInformation.investors.InvestorResponse;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.model.entity.businessInformation.BusinessDetail;
import com.EIPplatform.model.entity.businessInformation.investors.Investor;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;
import com.EIPplatform.util.StringNormalizerUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class ReportInvestorDetailServiceImpl implements ReportInvestorDetailService {

    ReportB04Repository reportB04Repository;
    ReportInvestorDetailMapper reportInvestorDetailMapper;
    ExceptionFactory exceptionFactory;
    BusinessDetailRepository businessDetailRepository;
    InvestorMapper investorMapper;
    ThirdPartyImplementerMapper thirdPartyImplementerMapper;
    ProjectMapper projectMapper;

    private final ReportCacheService<ReportB04DraftDTO> reportCacheService;

    // Field khởi tạo trong @PostConstruct
    @Autowired
    public ReportInvestorDetailServiceImpl(
            ReportB04Repository reportB04Repository,
            BusinessDetailRepository businessDetailRepository,
            ReportInvestorDetailMapper reportInvestorDetailMapper,
            ExceptionFactory exceptionFactory,
            FileStorageService fileStorageService,
            ReportCacheFactory reportCacheFactory, InvestorMapper investorMapper,
            ThirdPartyImplementerMapper thirdPartyImplementerMapper, ProjectMapper projectMapper) {

        this.reportB04Repository = reportB04Repository;
        this.businessDetailRepository = businessDetailRepository;
        this.reportInvestorDetailMapper = reportInvestorDetailMapper;
        this.exceptionFactory = exceptionFactory;
        this.investorMapper = investorMapper;
        this.thirdPartyImplementerMapper = thirdPartyImplementerMapper;
        this.projectMapper = projectMapper;
        // Lấy cache service qua factory
        this.reportCacheService = reportCacheFactory.getCacheService(ReportB04DraftDTO.class);
    }

    @Override
    @Transactional
    public ReportInvestorDetailDTO createReportInvestorDetailDTO(UUID reportB04Id, UUID businessDetailId,
            ReportInvestorDetailCreateRequest request) {
        request = StringNormalizerUtil.normalizeRequest(request);

        ReportB04 report = reportB04Repository.findById(reportB04Id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                "ReportB04",
                "reportB04Id",
                reportB04Id,
                ReportError.REPORT_NOT_FOUND));
        ReportInvestorDetail entity = reportInvestorDetailMapper.toEntityFromCreate(request);

        Investor investorEntity = null;
        if (request.getInvestorIndividual() != null) {
            investorEntity = investorMapper.toEntity(request.getInvestorIndividual());
        } else if (request.getInvestorOrganization() != null) {
            investorEntity = investorMapper.toEntity(request.getInvestorOrganization());
        }
        entity.setInvestor(investorEntity);
        entity.setThirdPartyImplementer(
                thirdPartyImplementerMapper.toEntityFromCreate(request.getThirdPartyImplementer()));
        entity.setProject(projectMapper.toEntityFromCreate(request.getProject()));

        // Map entity sang DTO response
        ReportInvestorDetailDTO responseDto = reportInvestorDetailMapper.toDTO(entity);
        reportCacheService.updateSectionData(reportB04Id, businessDetailId, responseDto,
                "ReportInvestorDetail");

        log.info("created reportInvestorDetail in cache - reportB04Id: {}, businessDetailId: {}", reportB04Id,
                businessDetailId);
        return responseDto; // FE phải lưu reportId để lần sau gửi tiếp các phần khác
    }

    @Override
    @Transactional(readOnly = true) // hàm này lấy tiếp dữ liệu từ cache report
    public ReportInvestorDetailDTO getReportInvestorDetailDTO(UUID reportId, UUID businessDetailId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);
        ReportInvestorDetailDTO reportInvestorDetailDTO = new ReportInvestorDetailDTO();
        if (draft == null || draft.getReportInvestorDetail() == null) { // nếu chưa có trong cache thì lấy dữ liệu ban đầu từ business detail
            reportInvestorDetailDTO = getInitialReportInvestorDetailDTO(businessDetailId);
            return reportInvestorDetailDTO;
        }
        log.info("getReportInvestorDetailDTO- reportId: {}, businessDetailId: {}", reportId, businessDetailId);
        return draft.getReportInvestorDetail();

    }

    private ReportInvestorDetailDTO getInitialReportInvestorDetailDTO(UUID businessDetailId) {
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
        // dto.setLegalDoc(legalDocMapper.toDto(businessDetail.getLegalDoc()));
        // }
        // // Third Party Implementer (nếu có)
        // if (businessDetail.getThirdPartyImplementer() != null) {
        // dto.setThirdPartyImplementer(
        // thirdPartyImplementerMapper.toDto(businessDetail.getThirdPartyImplementer()));
        // }
        // // Project (nếu có)
        // if (businessDetail.getProject() != null) {
        // dto.setProject(projectMapper.toDto(businessDetail.getProject()));
        // }
        return dto;

    }

}
