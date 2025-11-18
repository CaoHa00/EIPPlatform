package com.EIPplatform.service.report.reportB04.part1;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.AirEmissionError;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.businessInformation.ProductMapper;
import com.EIPplatform.mapper.report.reportB04.part1.ReportInvestorDetailMapper;
import com.EIPplatform.mapper.report.reportB04.part3.ResourcesSavingAndReductionMapper;
import com.EIPplatform.mapper.report.reportB04.part4.SymbiosisIndustryMapper;
import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.ReportInvestorDetailDTO;
import com.EIPplatform.model.dto.report.reportB04.part1.request.ReportInvestorDetailCreateRequest;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;
import com.EIPplatform.repository.businessInformation.BusinessDetailRepository;
import com.EIPplatform.repository.businessInformation.ProductRepository;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.repository.report.reportB04.part1.ReportInvestorDetailRepository;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheFactory;
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
public class ReportInvestorDetailServiceImpl implements ReportInvestorDetailService{
    ReportB04Repository reportB04Repository;
    BusinessDetailRepository businessDetailRepository;
    ReportInvestorDetailRepository reportInvestorDetailRepository;
    ReportInvestorDetailMapper reportInvestorDetailMapper;
    ProductRepository productRepository;
    ProductMapper productMapper;
    ExceptionFactory exceptionFactory;
    FileStorageService fileStorageService;
    ReportCacheFactory reportCacheFactory;
    ResourcesSavingAndReductionMapper resourcesSavingAndReductionMapper;
    SymbiosisIndustryMapper symbiosisIndustryMapper;
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

        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        ReportInvestorDetail entity = reportInvestorDetailMapper.toEntityFromCreate(request);


        ReportInvestorDetailDTO responseDto = reportInvestorDetailMapper.toDTO(entity);

        if (draft == null) {
            draft = ReportB04DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
            reportCacheService.saveDraftReport(draft, userAccountId, reportId);
        }

        reportCacheService.updateSectionData(reportId, userAccountId, responseDto, "airEmissionData");

        log.info("Created AirEmissionData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ReportInvestorDetailDTO getReportInvestorDetailDTO(UUID reportId, UUID userAccountId) {
         ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getReportInvestorDetailDTO() != null) {
            log.info("Found AirEmissionData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft.getReportInvestorDetailDTO();
        }

        log.warn("AirEmissionData not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return null;
    }
    
}