package com.EIPplatform.service.report.reportB04.part3;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.reportB04.part3.ResourcesSavingAndReductionMapper;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionCreateRequestDTO;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.model.entity.report.reportB04.part03.ResourcesSavingAndReduction;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;
import com.EIPplatform.util.StringNormalizerUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourcesSavingAndReductionServiceImpl implements ResourcesSavingAndReductionService{
    ReportB04Repository reportB04Repository;
    ExceptionFactory exceptionFactory;
    ResourcesSavingAndReductionMapper resourcesSavingAndReductionMapper;
    @NonFinal
    ReportCacheService<ReportB04DraftDTO> reportCacheService;

    @Autowired
    public ResourcesSavingAndReductionServiceImpl (
            ReportB04Repository reportB04Repository,
            ExceptionFactory exceptionFactory,
            ResourcesSavingAndReductionMapper resourcesSavingAndReductionMapper,
            ReportCacheFactory reportCacheFactory
    ){
        this.reportB04Repository = reportB04Repository;
        this.exceptionFactory = exceptionFactory;
        this.resourcesSavingAndReductionMapper = resourcesSavingAndReductionMapper;
        this.reportCacheService = reportCacheFactory.getCacheService(ReportB04DraftDTO.class);
    }


    @Override
    @Transactional
    public ResourcesSavingAndReductionDTO createReportB04Part3(UUID reportId, UUID businessDetailId, ResourcesSavingAndReductionCreateRequestDTO request) {
        request = StringNormalizerUtil.normalizeRequest(request);
        ReportB04 report = reportB04Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportB04",
                        "reportId",
                        reportId,
                        ReportError.REPORT_NOT_FOUND));

        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);

        ResourcesSavingAndReduction entity = resourcesSavingAndReductionMapper.toEntityFromCreate(request); //Still not map reportB04

        ResourcesSavingAndReductionDTO responseDto = resourcesSavingAndReductionMapper.toDTO(entity);


        reportCacheService.updateSectionData(reportId, businessDetailId, responseDto, "resourcesSavingAndReductionDTO");
        log.info("Created ResourcesSavingAndReduction in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ResourcesSavingAndReductionDTO getReportB04Part3(UUID reportId, UUID businessDetailId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);
        if(draft != null && draft.getResourcesSavingAndReductionDTO() != null){
            log.info("Found ResourcesSavingAndReduction in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
            return draft.getResourcesSavingAndReductionDTO();
        }
        log.warn("ResourcesSavingAndReduction not found in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
        return null;
    }

    @Override
    public void deleteReportB04Part3(UUID reportId, UUID businessDetailId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);
        if(draft != null){
            ResourcesSavingAndReductionDTO dto = draft.getResourcesSavingAndReductionDTO();
            if(dto != null){
                reportCacheService.updateSectionData(reportId, businessDetailId, null, "resourcesSavingAndReductionDTO");
                log.info("Deleted ResourcesSavingAndReduction from cache - reportId: {}, businessDetailId: {}", reportId,
                        businessDetailId);
            } else {
                log.warn("No ResourcesSavingAndReduction found in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
            }
        } else {
            log.warn("No draft found in cache - reportId: {}, businessDetailId: {}", reportId, businessDetailId);
        }
    }
}
