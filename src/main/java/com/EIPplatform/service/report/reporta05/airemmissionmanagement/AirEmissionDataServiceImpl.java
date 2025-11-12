package com.EIPplatform.service.report.reporta05.airemmissionmanagement;

import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.exception.errorCategories.AirEmissionError;
import com.EIPplatform.mapper.report.report05.airemmissionmanagement.AirEmissionDataMapper;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.service.report.reportcache.ReportCacheService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.entity.report.report05.ReportA05;
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
    public AirEmissionDataDTO createAirEmissionData(UUID reportId, UUID userAccountId, AirEmissionDataCreateDTO request, MultipartFile file) {

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        "reportId",
                        reportId,
                        AirEmissionError.REPORT_NOT_FOUND
                ));

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getAirEmissionData() != null) {
            AirEmissionDataDTO oldDto = draft.getAirEmissionData();
            if (oldDto.getAirAutoStationMapFilePath() != null && file != null) {
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
        saveToCache(reportId, userAccountId, responseDto);

        log.info("Created AirEmissionData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public AirEmissionDataDTO getAirEmissionData(UUID reportId, UUID userAccountId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getAirEmissionData() != null) {
            log.info("Found AirEmissionData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft.getAirEmissionData();
        }

        log.warn("AirEmissionData not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return null;
    }

    @Override
    @Transactional
    public void deleteAirEmissionData(UUID reportId, UUID userAccountId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
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
            reportCacheService.saveDraftReport(draft, userAccountId);
            log.info("Deleted AirEmissionData from cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        } else {
            log.warn("No draft found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        }
    }

    @Override
    @Transactional
    public void deleteAirEmissionDataFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
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
        saveToCache(reportId, userAccountId, dto);

        log.info("Deleted map file for AirEmissionData - reportId: {}, userAccountId: {}", reportId, userAccountId);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadAirEmissionDataFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
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
    public boolean hasAirEmissionDataFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null || draft.getAirEmissionData() == null || draft.getAirEmissionData().getAirAutoStationMapFilePath() == null) {
            return false;
        }

        return fileStorageService.fileExists(draft.getAirEmissionData().getAirAutoStationMapFilePath());
    }

    // ==================== PRIVATE HELPERS ====================

    private String uploadMapFile(ReportA05 report, MultipartFile file) {
        int year = LocalDateTime.now().getYear();
        String fileName = "air-station-map-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String directory = "reports/" + report.getReportId() + "/air-emission/map/" + year;
        String filePath = fileStorageService.storeFile(directory, fileName, file);

        log.info("Uploaded map file to: {} for report: {}", filePath, report.getReportId());
        return filePath;
    }

    private void saveToCache(UUID reportId, UUID userAccountId, AirEmissionDataDTO data) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
        }

        draft.setAirEmissionData(data);
        reportCacheService.saveDraftReport(draft, userAccountId);
    }
}