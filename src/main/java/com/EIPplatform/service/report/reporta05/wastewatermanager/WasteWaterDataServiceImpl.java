package com.EIPplatform.service.report.reporta05.wastewatermanager;

import java.time.LocalDateTime;
import java.util.UUID;

import com.EIPplatform.exception.errorCategories.WasteWaterError;
import com.EIPplatform.mapper.report.report05.wastewatermanager.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataCreateDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
import com.EIPplatform.service.fileStorage.FileStorageService;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.reportCacheA05.ReportCacheService;
import com.EIPplatform.util.StringNormalizerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.model.dto.report.report05.ReportA05DraftDTO;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.repository.report.ReportA05Repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WasteWaterDataServiceImpl implements WasteWaterDataService {

    ReportA05Repository reportA05Repository;
    WasteWaterDataMapper wasteWaterDataMapper;
    ReportCacheFactory reportCacheFactory;
    ReportCacheService<ReportA05DraftDTO> reportCacheService;
    FileStorageService fileStorageService;
    ExceptionFactory exceptionFactory;

    @Autowired
    public WasteWaterDataServiceImpl(ReportA05Repository reportA05Repository,
                                     WasteWaterDataMapper wasteWaterDataMapper,
                                     ReportCacheFactory reportCacheFactory,
                                     FileStorageService fileStorageService,
                                     ExceptionFactory exceptionFactory) {
        this.reportA05Repository = reportA05Repository;
        this.wasteWaterDataMapper = wasteWaterDataMapper;
        this.reportCacheFactory = reportCacheFactory;
        this.fileStorageService = fileStorageService;
        this.exceptionFactory = exceptionFactory;

        this.reportCacheService = reportCacheFactory.getCacheService(ReportA05DraftDTO.class);
    }

    @Override
    @Transactional
    public WasteWaterDataDTO createWasteWaterData(
            UUID reportId,
            UUID userAccountId,
            WasteWaterDataCreateDTO request,
            MultipartFile connectionFile,
            MultipartFile mapFile) {

        request = StringNormalizerUtil.normalizeRequest(request);

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        "reportId",
                        reportId,
                        WasteWaterError.REPORT_NOT_FOUND
                ));

        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        WasteWaterDataDTO oldDto = (draft != null) ? draft.getWasteWaterData() : null;

        WasteWaterData entity = wasteWaterDataMapper.toEntity(request);

        if (connectionFile != null && !connectionFile.isEmpty()) {
            if (oldDto != null && oldDto.getConnectionDiagram() != null) {
                fileStorageService.deleteFile(oldDto.getConnectionDiagram());
                log.info("Deleted old connection diagram file: {}", oldDto.getConnectionDiagram());
            }
            String filePath = uploadConnectionDiagramFile(report, connectionFile);
            entity.setConnectionDiagram(filePath);
        } else if (oldDto != null && oldDto.getConnectionDiagram() != null) {
            entity.setConnectionDiagram(oldDto.getConnectionDiagram());
        }
        if (mapFile != null && !mapFile.isEmpty()) {
            if (oldDto != null && oldDto.getAutoStationMap() != null) {
                fileStorageService.deleteFile(oldDto.getAutoStationMap());
                log.info("Deleted old auto station map file: {}", oldDto.getAutoStationMap());
            }
            String filePath = uploadAutoStationMapFile(report, mapFile);
            entity.setAutoStationMap(filePath);
        } else if (oldDto != null && oldDto.getAutoStationMap() != null) {
            entity.setAutoStationMap(oldDto.getAutoStationMap());
        }

        WasteWaterDataDTO responseDto = wasteWaterDataMapper.toDto(entity);

        if (draft == null) {
            draft = ReportA05DraftDTO.builder()
                    .reportId(reportId)
                    .isDraft(true)
                    .lastModified(LocalDateTime.now())
                    .build();
            reportCacheService.saveDraftReport(draft, userAccountId, reportId);
        }

        reportCacheService.updateSectionData(reportId, userAccountId, responseDto, "wasteWaterData");

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
        if (draft != null) {
            WasteWaterDataDTO dto = draft.getWasteWaterData();
            if (dto != null) {
                deleteFiles(dto);
                reportCacheService.updateSectionData(reportId, userAccountId, null, "wasteWaterData");
                log.info("Deleted WasteWaterData from cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            } else {
                log.warn("No WasteWaterData found in cache - reportId: {}, userAccountId: {}", reportId, userAccountId);
            }
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
        reportCacheService.updateSectionData(reportId, userAccountId, dto, "wasteWaterData");

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
        reportCacheService.updateSectionData(reportId, userAccountId, dto, "wasteWaterData");

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
}