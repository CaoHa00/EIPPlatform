package com.EIPplatform.service.report.reporta05.wastewatermanager;

import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.exception.errorCategories.WasteWaterError;
import com.EIPplatform.mapper.report.report05.wastewatermanager.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
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
public class WasteWaterDataServiceImpl implements WasteWaterDataService {

    ReportA05Repository reportA05Repository;
    WasteWaterDataMapper wasteWaterDataMapper;
    ReportCacheService reportCacheService;
    FileStorageService fileStorageService;
    ExceptionFactory exceptionFactory;

    @Override
    @Transactional
    public WasteWaterDataDTO createWasteWaterData(
            UUID reportId,
            UUID userAccountId,
            WasteWaterDataCreateDTO request,
            MultipartFile connectionFile,
            MultipartFile mapFile) {

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        "reportId",
                        reportId,
                        WasteWaterError.REPORT_NOT_FOUND
                ));

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getWasteWaterData() != null) {
            WasteWaterDataDTO oldDto = draft.getWasteWaterData();
            deleteOldFiles(oldDto, connectionFile, mapFile);
        }

        WasteWaterData entity = wasteWaterDataMapper.toEntity(request);

        if (connectionFile != null && !connectionFile.isEmpty()) {
            String filePath = uploadConnectionDiagramFile(report, connectionFile);
            entity.setConnectionDiagram(filePath);
        }

        if (mapFile != null && !mapFile.isEmpty()) {
            String filePath = uploadAutoStationMapFile(report, mapFile);
            entity.setAutoStationMap(filePath);
        }

        WasteWaterDataDTO responseDto = wasteWaterDataMapper.toDto(entity);
        saveToCache(reportId, userAccountId, responseDto);

        log.info("Created WasteWaterData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return responseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public WasteWaterDataDTO getWasteWaterData(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getWasteWaterData() != null) {
            log.info("Found WasteWaterData in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            return draft.getWasteWaterData();
        }

        log.warn("WasteWaterData not found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        return null;
    }

    @Override
    @Transactional
    public void deleteWasteWaterData(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft != null && draft.getWasteWaterData() != null) {
            WasteWaterDataDTO dto = draft.getWasteWaterData();
            deleteFiles(dto);
        }

        if (draft != null) {
            draft.setWasteWaterData(null);
            reportCacheService.saveDraftReport(draft, userAccountId);
            log.info("Deleted WasteWaterData from cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        } else {
            log.warn("No draft found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
        }
    }

    @Override
    @Transactional
    public void deleteWasteWaterDataConnectionFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null || draft.getWasteWaterData() == null) {
            throw exceptionFactory.createNotFoundException(
                    "WasteWaterData",
                    "reportId",
                    reportId,
                    WasteWaterError.NOT_FOUND
            );
        }

        WasteWaterDataDTO dto = draft.getWasteWaterData();
        if (dto.getConnectionDiagram() == null) {
            throw exceptionFactory.createNotFoundException(
                    "ConnectionDiagramFile", "wasteWaterData", reportId.toString(), WasteWaterError.NOT_FOUND
            );
        }

        fileStorageService.deleteFile(dto.getConnectionDiagram());
        dto.setConnectionDiagram(null);
        saveToCache(reportId, userAccountId, dto);

        log.info("Deleted connection diagram file - reportId: {}, userAccountId: {}", reportId, userAccountId);
    }

    @Override
    @Transactional
    public void deleteWasteWaterDataMapFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null || draft.getWasteWaterData() == null) {
            throw exceptionFactory.createNotFoundException(
                    "WasteWaterData",
                    "reportId",
                    reportId,
                    WasteWaterError.NOT_FOUND
            );
        }

        WasteWaterDataDTO dto = draft.getWasteWaterData();
        if (dto.getAutoStationMap() == null) {
            throw exceptionFactory.createNotFoundException(
                    "AutoStationMapFile", "wasteWaterData", reportId.toString(), WasteWaterError.NOT_FOUND
            );
        }

        fileStorageService.deleteFile(dto.getAutoStationMap());
        dto.setAutoStationMap(null);
        saveToCache(reportId, userAccountId, dto);

        log.info("Deleted auto station map file - reportId: {}, userAccountId: {}", reportId, userAccountId);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadWasteWaterDataConnectionFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null || draft.getWasteWaterData() == null) {
            throw exceptionFactory.createNotFoundException(
                    "WasteWaterData",
                    "reportId",
                    reportId,
                    WasteWaterError.NOT_FOUND
            );
        }

        WasteWaterDataDTO dto = draft.getWasteWaterData();
        if (dto.getConnectionDiagram() == null) {
            throw exceptionFactory.createNotFoundException(
                    "ConnectionDiagramFile", "wasteWaterData", reportId.toString(), WasteWaterError.NOT_FOUND
            );
        }

        return fileStorageService.downloadFile(dto.getConnectionDiagram());
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadWasteWaterDataMapFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null || draft.getWasteWaterData() == null) {
            throw exceptionFactory.createNotFoundException(
                    "WasteWaterData",
                    "reportId",
                    reportId,
                    WasteWaterError.NOT_FOUND
            );
        }

        WasteWaterDataDTO dto = draft.getWasteWaterData();
        if (dto.getAutoStationMap() == null) {
            throw exceptionFactory.createNotFoundException(
                    "AutoStationMapFile", "wasteWaterData", reportId.toString(), WasteWaterError.NOT_FOUND
            );
        }

        return fileStorageService.downloadFile(dto.getAutoStationMap());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasWasteWaterDataConnectionFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null || draft.getWasteWaterData() == null || draft.getWasteWaterData().getConnectionDiagram() == null) {
            return false;
        }

        return fileStorageService.fileExists(draft.getWasteWaterData().getConnectionDiagram());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasWasteWaterDataMapFile(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null || draft.getWasteWaterData() == null || draft.getWasteWaterData().getAutoStationMap() == null) {
            return false;
        }

        return fileStorageService.fileExists(draft.getWasteWaterData().getAutoStationMap());
    }

    // ==================== PRIVATE HELPERS ====================

    private String uploadConnectionDiagramFile(ReportA05 report, MultipartFile file) {
        int year = LocalDateTime.now().getYear();
        String fileName = "connection-diagram-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String directory = "reports/" + report.getReportId() + "/wastewater/connection-diagram/" + year;
        String filePath = fileStorageService.storeFile(directory, fileName, file);

        log.info("Uploaded connection diagram file to: {} for report: {}", filePath, report.getReportId());
        return filePath;
    }

    private String uploadAutoStationMapFile(ReportA05 report, MultipartFile file) {
        int year = LocalDateTime.now().getYear();
        String fileName = "auto-station-map-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String directory = "reports/" + report.getReportId() + "/wastewater/auto-station-map/" + year;
        String filePath = fileStorageService.storeFile(directory, fileName, file);

        log.info("Uploaded auto station map file to: {} for report: {}", filePath, report.getReportId());
        return filePath;
    }

    private void saveToCache(UUID reportId, UUID userAccountId, WasteWaterDataDTO data) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
        }

        draft.setWasteWaterData(data);
        reportCacheService.saveDraftReport(draft, userAccountId);
    }

    private void deleteOldFiles(WasteWaterDataDTO oldDto, MultipartFile connectionFile, MultipartFile mapFile) {
        if (oldDto.getConnectionDiagram() != null && connectionFile != null) {
            try {
                fileStorageService.deleteFile(oldDto.getConnectionDiagram());
                log.info("Deleted old connection diagram file: {}", oldDto.getConnectionDiagram());
            } catch (Exception e) {
                log.warn("Failed to delete old connection diagram file", e);
            }
        }

        if (oldDto.getAutoStationMap() != null && mapFile != null) {
            try {
                fileStorageService.deleteFile(oldDto.getAutoStationMap());
                log.info("Deleted old auto station map file: {}", oldDto.getAutoStationMap());
            } catch (Exception e) {
                log.warn("Failed to delete old auto station map file", e);
            }
        }
    }

    private void deleteFiles(WasteWaterDataDTO dto) {
        if (dto.getConnectionDiagram() != null) {
            try {
                fileStorageService.deleteFile(dto.getConnectionDiagram());
                log.info("Deleted connection diagram file: {}", dto.getConnectionDiagram());
            } catch (Exception e) {
                log.warn("Failed to delete connection diagram file", e);
            }
        }

        if (dto.getAutoStationMap() != null) {
            try {
                fileStorageService.deleteFile(dto.getAutoStationMap());
                log.info("Deleted auto station map file: {}", dto.getAutoStationMap());
            } catch (Exception e) {
                log.warn("Failed to delete auto station map file", e);
            }
        }
    }
}