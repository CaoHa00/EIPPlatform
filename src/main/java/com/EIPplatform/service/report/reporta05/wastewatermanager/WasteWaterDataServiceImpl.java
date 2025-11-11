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
    public WasteWaterDataDTO createWasteWaterData(UUID reportId, WasteWaterDataCreateDTO request, MultipartFile connectionFile, MultipartFile mapFile) {

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        "reportId",
                        reportId,
                        WasteWaterError.REPORT_NOT_FOUND
                ));

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getWasteWaterData() != null) {
            WasteWaterDataDTO oldDto = draft.getWasteWaterData();
            if (oldDto.getConnectionDiagram() != null && connectionFile!=null) {
                try {
                    fileStorageService.deleteFile(oldDto.getConnectionDiagram());
                    log.info("Deleted old connection diagram file for WasteWaterData: {}", oldDto.getConnectionDiagram());
                } catch (Exception e) {
                    log.warn("Failed to delete old connection diagram file: {}", oldDto.getConnectionDiagram(), e);
                }
            }

            if (oldDto.getAutoStationMap() != null && connectionFile!=null) {
                try {
                    fileStorageService.deleteFile(oldDto.getAutoStationMap());
                    log.info("Deleted old auto station map file for WasteWaterData: {}", oldDto.getAutoStationMap());
                } catch (Exception e) {
                    log.warn("Failed to delete old auto station map file: {}", oldDto.getAutoStationMap(), e);
                }
            }
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
        saveToCache(reportId, responseDto);

        log.info("Created (or replaced) WasteWaterData in cache for reportId: {}", reportId);
        return responseDto;
    }


    @Override
    @Transactional(readOnly = true)
    public WasteWaterDataDTO getWasteWaterData(UUID reportId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getWasteWaterData() != null) {
            log.info("Found WasteWaterData in cache for reportId: {}", reportId);
            return draft.getWasteWaterData();
        }

        log.warn("WasteWaterData not found in cache for reportId: {}", reportId);
        return null;
    }

    @Override
    @Transactional
    public void deleteWasteWaterData(UUID reportId) {

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft != null && draft.getWasteWaterData() != null) {
            WasteWaterDataDTO dto = draft.getWasteWaterData();
            if (dto.getConnectionDiagram() != null) {
                try {
                    fileStorageService.deleteFile(dto.getConnectionDiagram());
                    log.info("Deleted connection diagram file for WasteWaterData: {}", dto.getConnectionDiagram());
                } catch (Exception e) {
                    log.warn("Failed to delete connection diagram file: {}", dto.getConnectionDiagram(), e);
                }
            }

            if (dto.getAutoStationMap() != null) {
                try {
                    fileStorageService.deleteFile(dto.getAutoStationMap());
                    log.info("Deleted auto station map file for WasteWaterData: {}", dto.getAutoStationMap());
                } catch (Exception e) {
                    log.warn("Failed to delete auto station map file: {}", dto.getAutoStationMap(), e);
                }
            }
        }

        if (draft != null) {
            draft.setWasteWaterData(null);
            reportCacheService.saveDraftReport(draft);
            log.info("Deleted WasteWaterData from cache for reportId: {}", reportId);
        } else {
            log.warn("No draft found in cache for reportId: {}", reportId);
        }
    }

    @Override
    @Transactional
    public void deleteWasteWaterDataConnectionFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
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
        saveToCache(reportId, dto);

        log.info("Deleted connection diagram file for WasteWaterData for reportId: {}", reportId);
    }

    @Override
    @Transactional
    public void deleteWasteWaterDataMapFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
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
        saveToCache(reportId, dto);

        log.info("Deleted auto station map file for WasteWaterData for reportId: {}", reportId);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadWasteWaterDataConnectionFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
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
    public Resource downloadWasteWaterDataMapFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
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
    public boolean hasWasteWaterDataConnectionFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null || draft.getWasteWaterData() == null || draft.getWasteWaterData().getConnectionDiagram() == null) {
            return false;
        }

        return fileStorageService.fileExists(draft.getWasteWaterData().getConnectionDiagram());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasWasteWaterDataMapFile(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null || draft.getWasteWaterData() == null || draft.getWasteWaterData().getAutoStationMap() == null) {
            return false;
        }

        return fileStorageService.fileExists(draft.getWasteWaterData().getAutoStationMap());
    }

    // ==================== PRIVATE HELPERS ====================

    private String uploadConnectionDiagramFile(ReportA05 report, MultipartFile file) {
        // Generate path: e.g., "reports/{reportId}/wastewater/connection-diagram/{year}/{filename}"
        int year = LocalDateTime.now().getYear(); // Or from report date if available
        String fileName = "connection-diagram-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String directory = "reports/" + report.getReportId() + "/wastewater/connection-diagram/" + year;
        String filePath = fileStorageService.storeFile(directory, fileName, file);

        log.info("Uploaded connection diagram file to: {} for report: {}", filePath, report.getReportId());
        return filePath;
    }

    private String uploadAutoStationMapFile(ReportA05 report, MultipartFile file) {
        // Generate path: e.g., "reports/{reportId}/wastewater/auto-station-map/{year}/{filename}"
        int year = LocalDateTime.now().getYear(); // Or from report date if available
        String fileName = "auto-station-map-" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
        String directory = "reports/" + report.getReportId() + "/wastewater/auto-station-map/" + year;
        String filePath = fileStorageService.storeFile(directory, fileName, file);

        log.info("Uploaded auto station map file to: {} for report: {}", filePath, report.getReportId());
        return filePath;
    }

    private void saveToCache(UUID reportId, WasteWaterDataDTO data) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
        }

        draft.setWasteWaterData(data);
        reportCacheService.saveDraftReport(draft);
    }
}