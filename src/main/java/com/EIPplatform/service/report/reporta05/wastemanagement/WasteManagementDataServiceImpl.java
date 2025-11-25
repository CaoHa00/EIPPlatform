package com.EIPplatform.service.report.reporta05.wastemanagement;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.WasteManagementError;
import com.EIPplatform.mapper.report.report05.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;
import com.EIPplatform.util.StringNormalizerUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WasteManagementDataServiceImpl implements WasteManagementDataService {

    ReportA05Repository reportA05Repository;
    WasteManagementDataMapper wasteManagementDataMapper;
    ReportCacheFactory reportCacheFactory;
    ReportCacheService<ReportA05DraftDTO> reportCacheService;
    ExceptionFactory exceptionFactory;

    @Autowired
    public WasteManagementDataServiceImpl(ReportA05Repository reportA05Repository,
            WasteManagementDataMapper wasteManagementDataMapper,
            ReportCacheFactory reportCacheFactory,
            ExceptionFactory exceptionFactory) {
        this.reportA05Repository = reportA05Repository;
        this.wasteManagementDataMapper = wasteManagementDataMapper;
        this.reportCacheFactory = reportCacheFactory;
        this.exceptionFactory = exceptionFactory;

        this.reportCacheService = reportCacheFactory.getCacheService(ReportA05DraftDTO.class);
    }

    @Override
    @Transactional
    public WasteManagementDataDTO createWasteManagementData(UUID reportId, UUID businessDetailId,
            WasteManagementDataCreateDTO request) {

        request = StringNormalizerUtil.normalizeRequest(request);

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        "reportId",
                        reportId,
                        WasteManagementError.REPORT_NOT_FOUND));

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, businessDetailId);
        if (draft != null && draft.getWasteManagementData() != null) {
            log.info("Overwriting existing WasteManagementData in cache - reportId: {}, businessDetailId: {}",
                    reportId, businessDetailId);
        }

        WasteManagementData entity = wasteManagementDataMapper.toEntity(request);
        entity.setReport(report);

        WasteManagementDataDTO responseDto = wasteManagementDataMapper.toDto(entity);

        reportCacheService.updateSectionData(reportId, businessDetailId, responseDto, "wasteManagementData");

        log.info("Created WasteManagementData in cache - reportId: {}, businessDetailId: {}",
                reportId, businessDetailId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public WasteManagementDataDTO getWasteManagementData(UUID reportId, UUID userAccountId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getWasteManagementData() != null) {
            log.info("Found WasteManagementData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft.getWasteManagementData();
        }

        log.warn("WasteManagementData not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return null;
    }

    @Override
    @Transactional
    public void deleteWasteManagementData(UUID reportId, UUID userAccountId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null) {
            reportCacheService.updateSectionData(reportId, userAccountId, null, "wasteManagementData");
            log.info("Deleted WasteManagementData from cache - reportId: {}, userAccountId: {}", reportId,
                    userAccountId);
        } else {
            log.warn("No draft found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        }
    }
}