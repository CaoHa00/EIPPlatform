package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.report05.airemmissionmanagement.AirEmissionDataMapper;
import com.EIPplatform.mapper.report.report05.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.mapper.report.report05.wastewatermanager.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report05.*;
import com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.report05.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.report05.ReportA05;
import com.EIPplatform.model.entity.report.report05.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.report05.wastemanagement.WasteManagementData;
import com.EIPplatform.model.entity.report.report05.wastewatermanager.WasteWaterData;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.report.report05.airemmissionmanagement.AirEmissionDataRepository;
import com.EIPplatform.repository.report.report05.wastemanagement.WasteManagementDataRepository;
import com.EIPplatform.repository.report.report05.wastewatermanager.WasteWaterRepository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.service.report.reportcache.ReportCacheService;
import com.EIPplatform.util.ReportA05DocUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class ReportA05ServiceImpl implements ReportA05Service {

    ReportA05Repository reportA05Repository;
    BusinessDetailRepository businessDetailRepository;
    ReportCacheService reportCacheService;
    WasteManagementDataMapper wasteManagementDataMapper;
    AirEmissionDataMapper airEmissionDataMapper;
    WasteWaterDataMapper wasteWaterDataMapper;
    ExceptionFactory exceptionFactory;
    WasteManagementDataRepository wasteManagementDataRepository;
    AirEmissionDataRepository airEmissionDataRepository;
    WasteWaterRepository wasteWaterDataRepository;
    ReportA05DocUtil reportA05DocUtil;

    @NonFinal
    @Value("${app.storage.local.upload-dir:/app/uploads}")
    String uploadDir;

    // -------------------------------------------------------------
    // CRUD + draft logic (unchanged)
    // -------------------------------------------------------------

    @Override
    @Transactional
    public ReportA05DTO createReport(CreateReportRequest request) {
        BusinessDetail businessDetail = null;
        if (request.getBusinessDetailId() != null) {
            businessDetail = businessDetailRepository
                    .findById(request.getBusinessDetailId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException(
                            "BusinessDetail",
                            request.getBusinessDetailId(),
                            ReportError.BUSINESS_NOT_FOUND));
        }

        String reportCode = "RPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ReportA05 report = ReportA05.builder()
                .reportCode(reportCode)
                .businessDetail(businessDetail)
                .reportYear(request.getReportYear())
                .reportingPeriod(request.getReportingPeriod())
                .version(1)
                .isDeleted(false)
                .completionPercentage(0.0)
                .build();

        ReportA05 saved = reportA05Repository.save(report);

        return ReportA05DTO.builder()
                .reportId(saved.getReportId())
                .reportCode(saved.getReportCode())
                .businessDetailId(businessDetail != null ? businessDetail.getBusinessDetailId() : null)
                .facilityName(businessDetail != null ? businessDetail.getFacilityName() : null)
                .reportYear(saved.getReportYear())
                .reportingPeriod(saved.getReportingPeriod())
                .completionPercentage(saved.getCompletionPercentage())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public ReportA05DTO getReportById(UUID reportId) {
        ReportA05 report = reportA05Repository.findByReportIdWithBasic(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

        WasteWaterDataDTO wasteWaterDataDTO = wasteWaterDataRepository
                .findByReportIdWithCollections(reportId)
                .map(wasteWaterDataMapper::toDto)
                .orElse(null);

        WasteManagementDataDTO wasteManagementDataDTO = wasteManagementDataRepository
                .findByReportIdWithCollections(reportId)
                .map(wasteManagementDataMapper::toDto)
                .orElse(null);

        AirEmissionDataDTO airEmissionDataDTO = airEmissionDataRepository
                .findByReportIdWithCollections(reportId)
                .map(airEmissionDataMapper::toDto)
                .orElse(null);

        return ReportA05DTO.builder()
                .reportId(report.getReportId())
                .reportCode(report.getReportCode())
                .businessDetailId(report.getBusinessDetail() != null
                        ? report.getBusinessDetail().getBusinessDetailId()
                        : null)
                .facilityName(report.getBusinessDetail() != null
                        ? report.getBusinessDetail().getFacilityName()
                        : null)
                .reportYear(report.getReportYear())
                .reportingPeriod(report.getReportingPeriod())
                .wasteWaterData(wasteWaterDataDTO)
                .wasteManagementData(wasteManagementDataDTO)
                .airEmissionData(airEmissionDataDTO)
                .reviewNotes(report.getReviewNotes())
                .inspectionRemedyReport(report.getInspectionRemedyReport())
                .completionPercentage(report.getCompletionPercentage())
                .createdAt(report.getCreatedAt())
                .build();
    }

    @Override
    public ReportA05DraftDTO getDraftData(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId, userAccountId);
        return draft;
    }

    @Override
    @Transactional
    public ReportA05DraftDTO updateDraftCompletion(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draft = getDraftData(reportId, userAccountId);
        if (draft == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }
        int percentage = calculateCompletionPercentage(draft);
        draft.setCompletionPercentage(percentage);
        draft.setLastModified(LocalDateTime.now());
        reportCacheService.saveDraftReport(draft, userAccountId);
        log.info("Updated completion for report {} (user {}): {}%", reportId, userAccountId, percentage);
        return draft;
    }

    @Override
    @Transactional
    public ReportA05DTO submitDraftToDatabase(UUID reportId, UUID userAccountId) {
        ReportA05DraftDTO draftData = getDraftData(reportId, userAccountId);
        if (draftData == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }

        if (draftData.getCompletionPercentage() == null) {
            int percentage = calculateCompletionPercentage(draftData);
            draftData.setCompletionPercentage(percentage);
        }

        if (!isDraftComplete(draftData)) {
            throw exceptionFactory.createValidationException(
                    "ReportA05Draft",
                    "completionPercentage",
                    draftData.getCompletionPercentage() != null ? draftData.getCompletionPercentage() : 0,
                    ReportError.DRAFT_INCOMPLETE);
        }

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

        saveOrUpdateWasteWaterData(report, draftData);
        saveOrUpdateWasteManagementData(report, draftData);
        saveOrUpdateAirEmissionData(report, draftData);

        if (draftData.getCompletionPercentage() != null) {
            report.setCompletionPercentage(Double.valueOf(draftData.getCompletionPercentage()));
        }

        ReportA05 saved = reportA05Repository.save(report);

        draftData.setIsDraft(false);
        draftData.setLastModified(LocalDateTime.now());
        reportCacheService.deleteDraftReport(reportId, userAccountId);

        WasteManagementDataDTO wasteManagementDataDTO = null;
        if (saved.getWasteManagementData() != null) {
            wasteManagementDataDTO = wasteManagementDataMapper.toDto(saved.getWasteManagementData());
        }

        AirEmissionDataDTO airEmissionDataDTO = null;
        if (saved.getAirEmissionData() != null) {
            airEmissionDataDTO = airEmissionDataMapper.toDto(saved.getAirEmissionData());
        }

        WasteWaterDataDTO wasteWaterDataDTO = null;
        if (saved.getWasteWaterData() != null) {
            wasteWaterDataDTO = wasteWaterDataMapper.toDto(saved.getWasteWaterData());
        }

        BusinessDetail bd = saved.getBusinessDetail();
        return ReportA05DTO.builder()
                .reportId(saved.getReportId())
                .reportCode(saved.getReportCode())
                .businessDetailId(bd != null ? bd.getBusinessDetailId() : null)
                .facilityName(bd != null ? bd.getFacilityName() : null)
                .reportYear(saved.getReportYear())
                .reportingPeriod(saved.getReportingPeriod())
                .reviewNotes(saved.getReviewNotes())
                .airEmissionData(airEmissionDataDTO)
                .wasteManagementData(wasteManagementDataDTO)
                .wasteWaterData(wasteWaterDataDTO)
                .inspectionRemedyReport(saved.getInspectionRemedyReport())
                .completionPercentage(saved.getCompletionPercentage())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public InspectionRemedyResponse updateInspectionRemedyReport(UUID reportId,
                                                                 UpdateInspectionRemedyReportRequest request) {
        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

        if (request == null) {
            throw exceptionFactory.createValidationException(
                    "UpdateInspectionRemedyReportRequest",
                    "request",
                    null,
                    ReportError.INVALID_REQUEST);
        }
        if (Objects.isNull(request.getInspectionRemedyReport())) {
            throw exceptionFactory.createValidationException(
                    "UpdateInspectionRemedyReportRequest",
                    "inspectionRemedyReport",
                    null,
                    ReportError.FIELD_REQUIRED);
        }

        report.setInspectionRemedyReport(request.getInspectionRemedyReport());
        report.setUpdatedAt(LocalDateTime.now());

        ReportA05 saved = reportA05Repository.save(report);
        log.info("Updated inspection remedy report for reportId: {}", reportId);

        return InspectionRemedyResponse.builder()
                .reportId(saved.getReportId())
                .inspectionRemedyReport(saved.getInspectionRemedyReport())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    // -------------------------------------------------------------
    // Generate DOCX report – now delegated to ReportA05DocUtil
    // -------------------------------------------------------------

    @Override
    public byte[] generateReportFile(UUID reportId, UUID userAccountId) throws Exception {
        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05",
                        reportId,
                        ReportError.REPORT_NOT_FOUND));

        // Still save the file on disk here (IO concerns stay in the service)
        ReportA05DTO reportDTO = ReportA05DTO.builder()
                .reportYear(report.getReportYear())
                .build();

        ReportA05DraftDTO draftData = getDraftData(reportId, userAccountId);

        byte[] result = reportA05DocUtil.generateReportDocument(report, draftData);


        String savedFilePath = saveReportFile(result, reportId, report.getBusinessDetail(), reportDTO);
        log.info("✅ Report file generated and saved: {} ({} bytes)", savedFilePath, result.length);

        return result;
    }

    // -------------------------------------------------------------
    // Internal helpers (business/draft, not DOCX-specific)
    // -------------------------------------------------------------

    private int calculateCompletionPercentage(ReportA05DraftDTO draft) {
        if (draft.getWasteWaterData() != null
                && draft.getWasteManagementData() != null
                && draft.getAirEmissionData() != null) {
            return 100;
        }
        return 0;
    }

    private boolean isDraftComplete(ReportA05DraftDTO draftData) {
        return draftData.getWasteWaterData() != null
                && draftData.getWasteManagementData() != null
                && draftData.getAirEmissionData() != null;
    }

    private void saveOrUpdateWasteWaterData(ReportA05 report, ReportA05DraftDTO draftData) {
        WasteWaterDataDTO dto = draftData.getWasteWaterData();
        if (dto == null) return;

        WasteWaterData entity;
        if (report.getWasteWaterData() != null && report.getWasteWaterData().getWwId() != null) {
            entity = report.getWasteWaterData();
            wasteWaterDataMapper.updateEntityFromDto(dto, entity);
        } else {
            entity = wasteWaterDataMapper.dtoToEntity(dto);
            entity.setReport(report);
        }
        report.setWasteWaterData(entity);
    }

    private void saveOrUpdateWasteManagementData(ReportA05 report, ReportA05DraftDTO draftData) {
        WasteManagementDataDTO dto = draftData.getWasteManagementData();
        if (dto == null) return;

        WasteManagementData entity;
        if (report.getWasteManagementData() != null && report.getWasteManagementData().getWmId() != null) {
            entity = report.getWasteManagementData();
            wasteManagementDataMapper.updateEntityFromDto(dto, entity);
        } else {
            entity = wasteManagementDataMapper.dtoToEntity(dto);
            entity.setReport(report);
        }
        report.setWasteManagementData(entity);
    }

    private void saveOrUpdateAirEmissionData(ReportA05 report, ReportA05DraftDTO draftData) {
        AirEmissionDataDTO dto = draftData.getAirEmissionData();
        if (dto == null) return;

        AirEmissionData entity;
        if (report.getAirEmissionData() != null && report.getAirEmissionData().getAirEmissionDataId() != null) {
            entity = report.getAirEmissionData();
            airEmissionDataMapper.updateEntityFromDto(dto, entity);
        } else {
            entity = airEmissionDataMapper.dtoToEntity(dto);
            entity.setReport(report);
        }
        report.setAirEmissionData(entity);
    }

    // -------------------------------------------------------------
    // File saving stays here (IO = service responsibility)
    // -------------------------------------------------------------

    private String saveReportFile(byte[] fileBytes,
                                  UUID reportId,
                                  BusinessDetail business,
                                  ReportA05DTO report) {
        try {
            Integer reportYear = report.getReportYear() != null
                    ? report.getReportYear()
                    : LocalDateTime.now().getYear();

            Path reportDir = Paths.get(uploadDir, "reporta05", String.valueOf(reportYear));
            Files.createDirectories(reportDir);
            log.info("📁 Report directory: {}", reportDir);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String facilityName = business.getFacilityName() != null
                    ? sanitizeFileName(business.getFacilityName())
                    : "Unknown";

            String fileName = String.format("BaoCaoA05_%s_%s_%s.docx",
                    facilityName,
                    reportId.toString().substring(0, 8),
                    timestamp);

            Path filePath = reportDir.resolve(fileName);
            Files.write(filePath, fileBytes);

            String relativePath = String.format("reporta05/%d/%s", reportYear, fileName);
            log.info(" File saved successfully: {}", relativePath);

            return relativePath;

        } catch (IOException e) {
            log.error("⚠️ Could not save report file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save report file", e);
        }
    }

    private String sanitizeFileName(String input) {
        if (input == null || input.isEmpty()) {
            return "Unknown";
        }

        String sanitized = input
                .replaceAll("[/\\\\:*?\"<>|]", "")
                .replaceAll("\\s+", "_")
                .trim();

        if (sanitized.length() > 50) {
            sanitized = sanitized.substring(0, 50);
        }
        return sanitized;
    }
}
