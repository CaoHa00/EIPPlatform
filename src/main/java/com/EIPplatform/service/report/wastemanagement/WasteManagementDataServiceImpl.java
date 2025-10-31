package com.EIPplatform.service.report.wastemanagement;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.WasteManagementError;
import com.EIPplatform.mapper.report.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataCreateDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataUpdateDTO;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.report.wastemanagement.WasteManagementDataRepository;
import com.EIPplatform.service.report.ReportCacheService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WasteManagementDataServiceImpl implements WasteManagementDataService {

    WasteManagementDataRepository wasteManagementDataRepository;
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

        if (wasteManagementDataRepository.existsByReportId(reportId)) {
            throw exceptionFactory.createAlreadyExistsException(
                    "WasteManagementData",
                    "reportId",
                    reportId,
                    WasteManagementError.DUPLICATE_ENTRY
            );
        }

        WasteManagementData entity = wasteManagementDataMapper.toEntity(request);
        entity.setReport(report);

        WasteManagementDataDTO responseDto = wasteManagementDataMapper.toDto(entity);
        saveToCache(reportId, responseDto);

        log.info("Created WasteManagementData for reportId: {}", reportId);
        return responseDto;
    }

    @Override
    @Transactional
    public WasteManagementDataDTO updateWasteManagementData(UUID reportId, WasteManagementDataUpdateDTO request) {

        WasteManagementDataDTO currentData = getWasteManagementData(reportId);
        if (currentData == null) {
            throw exceptionFactory.createNotFoundException(
                    "WasteManagementData",
                    "reportId",
                    reportId,
                    WasteManagementError.NOT_FOUND
            );
        }

        WasteManagementData entity = wasteManagementDataMapper.dtoToEntity(currentData);

        wasteManagementDataMapper.updateEntityFromDto(request, entity);

        WasteManagementDataDTO responseDto = wasteManagementDataMapper.toDto(entity);
        saveToCache(reportId, responseDto);

        log.info("Updated WasteManagementData for reportId: {}", reportId);
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

        WasteManagementData entity = wasteManagementDataRepository.findByReportId(reportId)
                .orElse(null);

        if (entity == null) {
            log.warn("WasteManagementData not found for reportId: {}", reportId);
            return null;
        }

        log.info("Found WasteManagementData in DB for reportId: {}", reportId);
        WasteManagementDataDTO dto = wasteManagementDataMapper.toDto(entity);

        saveToCache(reportId, dto);

        return dto;
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