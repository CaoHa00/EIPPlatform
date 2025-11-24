package com.EIPplatform.service.report.reportB04.part3;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ResourcesSavingAndReductionError;
import com.EIPplatform.exception.errorCategories.WasteWaterError;
import com.EIPplatform.mapper.report.reportB04.part3.ResourcesSavingAndReductionMapper;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.ResourcesSavingAndReductionDTO;
import com.EIPplatform.model.dto.report.reportB04.part3.request.ResourcesSavingAndReductionCreateRequestDTO;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;
import com.EIPplatform.model.entity.report.reportB04.part03.ResourcesSavingAndReduction;
import com.EIPplatform.repository.report.reportB04.ReportB04Repository;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheService;
import com.EIPplatform.service.report.reportCache.reportCacheB04.ReportB04CacheService;
import com.EIPplatform.util.StringNormalizerUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourcesSavingAndReductionImpl implements ResourcesSavingAndReductionService{
    ReportB04Repository reportB04Repository;
    ResourcesSavingAndReductionMapper resourcesSavingAndReductionMapper;
    ReportB04CacheService reportCacheService;
    ExceptionFactory exceptionFactory;


    @Override
    @Transactional
    public ResourcesSavingAndReductionDTO createResourcesSavingAndReduction(UUID reportId, UUID userAccountId, ResourcesSavingAndReductionCreateRequestDTO request) {
        request = StringNormalizerUtil.normalizeRequest(request);
        ReportB04 report = reportB04Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportB04",
                        "reportId",
                        reportId,
                        ResourcesSavingAndReductionError.REPORT_NOT_FOUND));

        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
//        ResourcesSavingAndReductionDTO oldDto = (draft != null) ? draft.getResourcesSavingAndReductionDTO() : null;

        ResourcesSavingAndReduction entity = resourcesSavingAndReductionMapper.toEntityFromCreate(request); //Still not map reportB04

        ResourcesSavingAndReductionDTO responseDto = resourcesSavingAndReductionMapper.toDTO(entity);

        if(draft == null){
            draft = ReportB04DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
            reportCacheService.saveDraftReport(draft, userAccountId); //different from cache 05
        }

        //missing updateSectionData here: reportCacheService.updateSectionData(reportId, userAccountId, responseDto, "wasteWaterData");
        log.info("Created ResourcesSavingAndReduction in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public ResourcesSavingAndReductionDTO getResourcesSavingAndReduction(UUID reportId, UUID userAccountId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if(draft != null && draft.getResourcesSavingAndReductionDTO() != null){
            log.info("Found ResourcesSavingAndReduction in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft.getResourcesSavingAndReductionDTO();
        }
        log.warn("ResourcesSavingAndReduction not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return null;
    }

    @Override
    public void deleteResourcesSavingAndReduction(UUID reportId, UUID userAccountId) {
        ReportB04DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if(draft != null){
            ResourcesSavingAndReductionDTO dto = draft.getResourcesSavingAndReductionDTO();
            if(dto != null){
                //missing updateSectionData here: reportCacheService.updateSectionData(reportId, userAccountId, null, "wasteWaterData");
                log.info("Deleted ResourcesSavingAndReduction from cache - reportId: {}, userAccountId: {}", reportId,
                        userAccountId);
            } else {
                log.warn("No ResourcesSavingAndReduction found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            }
        } else {
            log.warn("No draft found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        }
    }



}
