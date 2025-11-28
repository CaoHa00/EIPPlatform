package com.EIPplatform.service.report.reportB04.part4;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.reportB04.part4.SymbiosisIndustryMapper;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.SymbiosisIndustryDTO;
import com.EIPplatform.model.dto.report.reportB04.part4.request.SymbiosisIndustryCreateRequestDTO;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.model.entity.report.reportB04.part04.SymbiosisIndustry;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;
import com.EIPplatform.util.StringNormalizerUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SymbiosisIndustryServiceImpl implements SymbiosisIndustryService{
    ReportB04Repository reportB04Repository;
    ExceptionFactory exceptionFactory;
    SymbiosisIndustryMapper symbiosisIndustryMapper;
    @NonFinal
    ReportCacheService<ReportB04DraftDTO> reportCacheService;

    @Autowired
    public SymbiosisIndustryServiceImpl (
            ReportB04Repository reportB04Repository,
            ExceptionFactory exceptionFactory,
            SymbiosisIndustryMapper symbiosisIndustryMapper,
            ReportCacheFactory reportCacheFactory
    ){
        this.reportB04Repository = reportB04Repository;
        this.exceptionFactory = exceptionFactory;
        this.symbiosisIndustryMapper = symbiosisIndustryMapper;
        this.reportCacheService = reportCacheFactory.getCacheService(ReportB04DraftDTO.class);
    }
    @Override
    public SymbiosisIndustryDTO createReportB04Part4(UUID reportId, UUID businessDetailId, SymbiosisIndustryCreateRequestDTO request) {
        request = StringNormalizerUtil.normalizeRequest(request);
        ReportB04 report = reportB04Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportB04",
                        "reportId",
                        reportId,
                        ReportError.REPORT_NOT_FOUND));

        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);

        SymbiosisIndustry entity = symbiosisIndustryMapper.toEntityFromCreate(request); //Still not map reportB04

        SymbiosisIndustryDTO responseDto = symbiosisIndustryMapper.toDTO(entity);


        reportCacheService.updateSectionData(reportId, businessDetailId, responseDto, "symbiosisIndustryDTO");
        log.info("Created SymbiosisIndustry in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
        return responseDto;
    }

    @Override
    public SymbiosisIndustryDTO getReportB04Part4(UUID reportId, UUID businessDetailId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);
        if(draft != null && draft.getSymbiosisIndustry() != null){
            log.info("Found SymbiosisIndustry in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
            return draft.getSymbiosisIndustry();
        }
        log.warn("SymbiosisIndustry not found in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
        return null;
    }

    @Override
    public void deleteReportB04Part4(UUID reportId, UUID businessDetailId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);
        if(draft != null){
            SymbiosisIndustryDTO dto = draft.getSymbiosisIndustry();
            if(dto != null){
                reportCacheService.updateSectionData(reportId, businessDetailId, null, "symbiosisIndustryDTO");
                log.info("Deleted SymbiosisIndustry from cache - reportId: {}, businessDetailId: {}", reportId,
                        businessDetailId);
            } else {
                log.warn("No SymbiosisIndustry found in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
            }
        } else {
            log.warn("No draft found in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
        }
    }
}
