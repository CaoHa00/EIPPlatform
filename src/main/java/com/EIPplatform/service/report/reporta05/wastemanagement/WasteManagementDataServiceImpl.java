package com.EIPplatform.service.report.reporta05.wastemanagement;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.WasteManagementError;
import com.EIPplatform.mapper.report.report05.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.service.report.reportcache.ReportCacheService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WasteManagementDataServiceImpl implements WasteManagementDataService {

    ReportA05Repository reportA05Repository;
    WasteManagementDataMapper wasteManagementDataMapper;
    ReportCacheService reportCacheService;
    ExceptionFactory exceptionFactory;

    @Override
    @Transactional
    public WasteManagementDataDTO createWasteManagementData(UUID reportId, WasteManagementDataCreateDTO request) {

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        "reportId",
                        reportId,
                        WasteManagementError.REPORT_NOT_FOUND
                ));

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getWasteManagementData() != null) {
            log.info("Overwriting existing WasteManagementData in cache for reportId: {}", reportId);
        }

        WasteManagementData entity = wasteManagementDataMapper.toEntity(request);
        entity.setReport(report);

        WasteManagementDataDTO responseDto = wasteManagementDataMapper.toDto(entity);
        saveToCache(reportId, responseDto);

        log.info("Created (or replaced) WasteManagementData in cache for reportId: {}", reportId);
        return responseDto;
    }



    @Override
    @Transactional(readOnly = true)
    public WasteManagementDataDTO getWasteManagementData(UUID reportId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getWasteManagementData() != null) {
            log.info("Found WasteManagementData in cache for reportId: {}", reportId);
            return draft.getWasteManagementData();
        }

        log.warn("WasteManagementData not found in cache for reportId: {}", reportId);
        return null;
    }

    @Override
    @Transactional
    public void deleteWasteManagementData(UUID reportId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null) {
            draft.setWasteManagementData(null);
            reportCacheService.saveDraftReport(draft);
            log.info("Deleted WasteManagementData from cache for reportId: {}", reportId);
        } else {
            log.warn("No draft found in cache for reportId: {}", reportId);
        }
    }

    private void saveToCache(UUID reportId, WasteManagementDataDTO data) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
        }

        draft.setWasteManagementData(data);
        reportCacheService.saveDraftReport(draft);
    }
}