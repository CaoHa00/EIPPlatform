package com.EIPplatform.service.report.report06.part03;


import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.exception.errorCategories.InventoryResultError;
import com.EIPplatform.mapper.report.report06.part03.InventoryResultDataMapper;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataCreateDTO;
import com.EIPplatform.model.dto.report.report06.part03.inventoryResultData.InventoryResultDataDTO;
import com.EIPplatform.model.entity.report.report06.part03.InventoryResultData;
import com.EIPplatform.repository.report.report06.Report06Repository;
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;
import com.EIPplatform.util.StringNormalizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.model.dto.report.report06.Report06DraftDTO;
import com.EIPplatform.model.entity.report.report06.Report06;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InventoryResultDataServiceImpl implements InventoryResultDataService {

    Report06Repository report06Repository;
    InventoryResultDataMapper inventoryResultDataMapper;
    ReportCacheFactory reportCacheFactory;
    ReportCacheService<Report06DraftDTO> reportCacheService;
    ExceptionFactory exceptionFactory;

    @Autowired
    public InventoryResultDataServiceImpl(Report06Repository report06Repository,
                                          InventoryResultDataMapper inventoryResultDataMapper,
                                          ReportCacheFactory reportCacheFactory,
                                          ExceptionFactory exceptionFactory) {
        this.report06Repository = report06Repository;
        this.inventoryResultDataMapper = inventoryResultDataMapper;
        this.reportCacheFactory = reportCacheFactory;
        this.exceptionFactory = exceptionFactory;

        this.reportCacheService = reportCacheFactory.getCacheService(Report06DraftDTO.class);
    }

    @Override
    @Transactional
    public InventoryResultDataDTO createInventoryResultData(UUID report06Id, UUID userAccountId, InventoryResultDataCreateDTO request) {

        request = StringNormalizerUtil.normalizeRequest(request);

        Report06 report = report06Repository.findById(report06Id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Report06",
                        "report06Id",
                        report06Id,
                        InventoryResultError.REPORT_NOT_FOUND));

        Report06DraftDTO draft = reportCacheService.getDraftReport(report06Id, userAccountId);
        if (draft != null && draft.getInventoryResultData() != null) {
            log.info("Overwriting existing InventoryResultData in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        }

        InventoryResultData entity = inventoryResultDataMapper.toEntity(request);
        entity.setReport06(report);

        InventoryResultDataDTO responseDto = inventoryResultDataMapper.toDTO(entity);

        if (draft == null) {
            draft = Report06DraftDTO.builder()
                    .report06Id(report06Id)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
            reportCacheService.saveDraftReport(draft, userAccountId, report06Id);
        }

        reportCacheService.updateSectionData(report06Id, userAccountId, responseDto, "inventoryResultData");

        log.info("Created InventoryResultData in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResultDataDTO getInventoryResultData(UUID report06Id, UUID userAccountId) {

        Report06DraftDTO draft = reportCacheService.getDraftReport(report06Id, userAccountId);
        if (draft != null && draft.getInventoryResultData() != null) {
            log.info("Found InventoryResultData in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
            return draft.getInventoryResultData();
        }

        log.warn("InventoryResultData not found in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        return null;
    }

    @Override
    @Transactional
    public void deleteInventoryResultData(UUID report06Id, UUID userAccountId) {

        Report06DraftDTO draft = reportCacheService.getDraftReport(report06Id, userAccountId);
        if (draft != null) {
            reportCacheService.updateSectionData(report06Id, userAccountId, null, "inventoryResultData");
            log.info("Deleted InventoryResultData from cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        } else {
            log.warn("No draft found in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        }
    }
}