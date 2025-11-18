package com.EIPplatform.service.report.report06.part02;

import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.exception.errorCategories.OperationalActivityError;
import com.EIPplatform.mapper.report.report06.part02.*;
import com.EIPplatform.model.dto.report.report06.part02.operationalActivityData.*;
import com.EIPplatform.model.entity.report.report06.part02.*;
import com.EIPplatform.repository.report.report06.Report06Repository;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheService;
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
public class OperationActivityDataServiceImpl  implements OperationActivityDataService {

    Report06Repository report06Repository;
    OperationalActivityDataMapper operationalActivityDataMapper;
    ReportCacheFactory reportCacheFactory;
    ReportCacheService<Report06DraftDTO> reportCacheService;
    ExceptionFactory exceptionFactory;

    @Autowired
    public OperationActivityDataServiceImpl(Report06Repository report06Repository,
                                            OperationalActivityDataMapper operationalActivityDataMapper,
                                            ReportCacheFactory reportCacheFactory,
                                            ExceptionFactory exceptionFactory) {
        this.report06Repository = report06Repository;
        this.operationalActivityDataMapper = operationalActivityDataMapper;
        this.reportCacheFactory = reportCacheFactory;
        this.exceptionFactory = exceptionFactory;

        this.reportCacheService = reportCacheFactory.getCacheService(Report06DraftDTO.class);
    }

    @Override
    @Transactional
    public OperationalActivityDataDTO createOperationalActivityData(UUID report06Id, UUID userAccountId, OperationalActivityDataCreateDTO request) {

        request = StringNormalizerUtil.normalizeRequest(request);

        Report06 report = report06Repository.findById(report06Id)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "Report06",
                        "report06Id",
                        report06Id,
                        OperationalActivityError.REPORT_NOT_FOUND));

        Report06DraftDTO draft = reportCacheService.getDraftReport(report06Id, userAccountId);
        if (draft != null && draft.getOperationalActivityData() != null) {
            log.info("Overwriting existing OperationalActivityData in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        }

        OperationalActivityData entity = operationalActivityDataMapper.toEntity(request);
        entity.setReport06(report);

        OperationalActivityDataDTO responseDto = operationalActivityDataMapper.toDTO(entity);

        if (draft == null) {
            draft = Report06DraftDTO.builder()
                    .report06Id(report06Id)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
            reportCacheService.saveDraftReport(draft, userAccountId, report06Id);
        }

        reportCacheService.updateSectionData(report06Id, userAccountId, responseDto, "operationalActivityData");

        log.info("Created OperationalActivityData in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public OperationalActivityDataDTO getOperationalActivityData(UUID report06Id, UUID userAccountId) {

        Report06DraftDTO draft = reportCacheService.getDraftReport(report06Id, userAccountId);
        if (draft != null && draft.getOperationalActivityData() != null) {
            log.info("Found OperationalActivityData in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
            return draft.getOperationalActivityData();
        }

        log.warn("OperationalActivityData not found in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        return null;
    }

    @Override
    @Transactional
    public void deleteOperationalActivityData(UUID report06Id, UUID userAccountId) {

        Report06DraftDTO draft = reportCacheService.getDraftReport(report06Id, userAccountId);
        if (draft != null) {
            reportCacheService.updateSectionData(report06Id, userAccountId, null, "operationalActivityData");
            log.info("Deleted OperationalActivityData from cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        } else {
            log.warn("No draft found in cache - report06Id: {}, userAccountId: {}", report06Id, userAccountId);
        }
    }
}