package com.EIPplatform.service.report.airemmissionmanagement;

import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.exception.errorCategories.AirEmissionError;
import com.EIPplatform.mapper.report.airemmissionmanagement.AirEmissionDataMapper;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.entity.report.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.service.report.reportcache.ReportCacheService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.repository.report.ReportA05Repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import lombok.AccessLevel;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AirEmissionDataServiceImpl implements AirEmissionDataService {

    ReportA05Repository reportA05Repository;
    AirEmissionDataMapper airEmissionDataMapper;
    ReportCacheService reportCacheService;
    FileStorageService fileStorageService;
    ExceptionFactory exceptionFactory;

    @Override
    @Transactional
    public AirEmissionDataDTO createAirEmissionData(UUID reportId, AirEmissionDataCreateDTO request, MultipartFile file) {

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        "reportId",
                        reportId,
                        AirEmissionError.REPORT_NOT_FOUND
                ));

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getAirEmissionData() != null) {
            AirEmissionDataDTO oldDto = draft.getAirEmissionData();
            if (oldDto.getAirAutoStationMapFilePath() != null && file !=null) {
                try {
                    fileStorageService.deleteFile(oldDto.getAirAutoStationMapFilePath());
                    log.info("Deleted old map file for AirEmissionData: {}", oldDto.getAirAutoStationMapFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete old map file: {}", oldDto.getAirAutoStationMapFilePath(), e);
                }
            }
        }

        AirEmissionData entity = airEmissionDataMapper.toEntity(request);

        if (file != null && !file.isEmpty()) {
            String filePath = uploadMapFile(report, file);
            entity.setAirAutoStationMapFilePath(filePath);
        }

        AirEmissionDataDTO responseDto = airEmissionDataMapper.toDto(entity);
        saveToCache(reportId, responseDto);

        log.info("Created (or replaced) AirEmissionData in cache for reportId: {}", reportId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public AirEmissionDataDTO getAirEmissionData(UUID reportId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getAirEmissionData() != null) {
            log.info("Found AirEmissionData in cache for reportId: {}", reportId);
            return draft.getAirEmissionData();
        }

        log.warn("AirEmissionData not found in cache for reportId: {}", reportId);
        return null;
    }

    @Override
    @Transactional
    public void deleteAirEmissionData(UUID reportId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getAirEmissionData() != null) {
            AirEmissionDataDTO dto = draft.getAirEmissionData();
            if (dto.getAirAutoStationMapFilePath() != null) {
                try {
                    fileStorageService.deleteFile(dto.getAirAutoStationMapFilePath());
                    log.info("Deleted map file for AirEmissionData: {}", dto.getAirAutoStationMapFilePath());
                } catch (Exception e) {
                    log.warn("Failed to delete map file: {}", dto.getAirAutoStationMapFilePath(), e);
                }
            }
        }

        if (draft != null) {
            draft.setAirEmissionData(null);
            reportCacheService.saveDraftReport(draft);
            log.info("Deleted AirEmissionData from cache for reportId: {}", reportId);
        } else {
            log.warn("No draft found in cache for reportId: {}", reportId);
        }
    }

    @Override
    @Transactional
    public void deleteAirEmissionDataFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null || draft.getAirEmissionData() == null) {
            throw exceptionFactory.createNotFoundException(
                    "AirEmissionData",
                    "reportId",
                    reportId,
                    AirEmissionError.NOT_FOUND
            );
        }

        AirEmissionDataDTO dto = draft.getAirEmissionData();
        if (dto.getAirAutoStationMapFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "MapFile", "airEmissionData", reportId.toString(), AirEmissionError.NOT_FOUND
            );
        }

        fileStorageService.deleteFile(dto.getAirAutoStationMapFilePath());
        dto.setAirAutoStationMapFilePath(null);
        saveToCache(reportId, dto);

        log.info("Deleted map file for AirEmissionData for reportId: {}", reportId);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadAirEmissionDataFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null || draft.getAirEmissionData() == null) {
            throw exceptionFactory.createNotFoundException(
                    "AirEmissionData",
                    "reportId",
                    reportId,
                    AirEmissionError.NOT_FOUND
            );
        }

        AirEmissionDataDTO dto = draft.getAirEmissionData();
        if (dto.getAirAutoStationMapFilePath() == null) {
            throw exceptionFactory.createNotFoundException(
                    "MapFile", "airEmissionData", reportId.toString(), AirEmissionError.NOT_FOUND
            );
        }

        return fileStorageService.downloadFile(dto.getAirAutoStationMapFilePath());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAirEmissionDataFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null || draft.getAirEmissionData() == null || draft.getAirEmissionData().getAirAutoStationMapFilePath() == null) {
            return false;
        }

        return fileStorageService.fileExists(draft.getAirEmissionData().getAirAutoStationMapFilePath());
    }

    // ==================== PRIVATE HELPERS ====================

    private String uploadMapFile(ReportA05 report, MultipartFile file) {
        // Generate path: e.g., "reports/{reportId}/air-map/{year}/{filename}"
        int year = LocalDateTime.now().getYear(); // Or from report date if available
        String fileName = "air-station-map-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String directory = "reports/" + report.getReportId() + "/air-emission/map/" + year;
        String filePath = fileStorageService.storeFile(directory, fileName, file);

        log.info("Uploaded map file to: {} for report: {}", filePath, report.getReportId());
        return filePath;
    }

    private void saveToCache(UUID reportId, AirEmissionDataDTO data) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
        }

        draft.setAirEmissionData(data);
        reportCacheService.saveDraftReport(draft);
    }
}