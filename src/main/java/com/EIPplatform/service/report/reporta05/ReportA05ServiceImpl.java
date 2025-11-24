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
import com.EIPplatform.service.report.reportCache.ReportCacheFactory;
import com.EIPplatform.service.report.reportCache.ReportCacheService;
import com.EIPplatform.util.ReportA05DocUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

        private final ReportA05Repository reportA05Repository;
        private final BusinessDetailRepository businessDetailRepository;
        private final ReportCacheFactory reportCacheFactory;
        private final ReportCacheService<ReportA05DraftDTO> reportCacheService;
        private final WasteManagementDataMapper wasteManagementDataMapper;
        private final AirEmissionDataMapper airEmissionDataMapper;
        private final WasteWaterDataMapper wasteWaterDataMapper;
        private final ExceptionFactory exceptionFactory;
        private final WasteManagementDataRepository wasteManagementDataRepository;
        private final AirEmissionDataRepository airEmissionDataRepository;
        private final WasteWaterRepository wasteWaterDataRepository;
    private ReportA05DocUtil reportA05DocUtil;

    @Autowired
        public ReportA05ServiceImpl(
                ReportA05Repository reportA05Repository,
                BusinessDetailRepository businessDetailRepository,
                ReportCacheFactory reportCacheFactory,
                WasteManagementDataMapper wasteManagementDataMapper,
                AirEmissionDataMapper airEmissionDataMapper,
                WasteWaterDataMapper wasteWaterDataMapper,
                ExceptionFactory exceptionFactory,
                WasteManagementDataRepository wasteManagementDataRepository,
                AirEmissionDataRepository airEmissionDataRepository,
                WasteWaterRepository wasteWaterDataRepository, ReportA05DocUtil reportA05DocUtil) {

                this.reportA05Repository = reportA05Repository;
                this.businessDetailRepository = businessDetailRepository;
                this.reportCacheFactory = reportCacheFactory;
                this.wasteManagementDataMapper = wasteManagementDataMapper;
                this.airEmissionDataMapper = airEmissionDataMapper;
                this.wasteWaterDataMapper = wasteWaterDataMapper;
                this.exceptionFactory = exceptionFactory;
                this.wasteManagementDataRepository = wasteManagementDataRepository;
                this.airEmissionDataRepository = airEmissionDataRepository;
                this.wasteWaterDataRepository = wasteWaterDataRepository;

                this.reportCacheService = reportCacheFactory.getCacheService(ReportA05DraftDTO.class);
        this.reportA05DocUtil = reportA05DocUtil;
    }
        @NonFinal
        @Value("${app.storage.local.upload-dir:/app/uploads}")
        private String uploadDir;

    @Override
    @Transactional
    public ReportA05DTO createReport(CreateReportRequest request) {
        // 1. Kiểm tra business (CHỈ KHI CÓ businessDetailId)
        BusinessDetail businessDetail = null;
        if (request.getBusinessDetailId() != null) {
            businessDetail = businessDetailRepository
                    .findById(request.getBusinessDetailId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail",
                            request.getBusinessDetailId(), ReportError.BUSINESS_NOT_FOUND));
        }

        String reportCode = "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

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

        // 1. Fetch basic report
        ReportA05 report = reportA05Repository.findByReportIdWithBasic(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException(
                        "ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

        // 2. Fetch WasteWaterData với collections
        WasteWaterDataDTO wasteWaterDataDTO = wasteWaterDataRepository
                .findByReportIdWithCollections(reportId)
                .map(wasteWaterDataMapper::toDto)
                .orElse(null);

        // 3. Fetch WasteManagementData với collections
        WasteManagementDataDTO wasteManagementDataDTO = wasteManagementDataRepository
                .findByReportIdWithCollections(reportId)
                .map(wasteManagementDataMapper::toDto)
                .orElse(null);

        // 4. Fetch AirEmissionData với collections
        AirEmissionDataDTO airEmissionDataDTO = airEmissionDataRepository
                .findByReportIdWithCollections(reportId)
                .map(airEmissionDataMapper::toDto)
                .orElse(null);

        // 5. Build DTO
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
        if (draft == null) {
            return null;
        }
        return draft;
    }

    @Override
    public ReportA05DTO submitDraftToDatabase(UUID reportId, UUID userAccountId) {
        return null;
    }

    /**
     * Cập nhật completion percentage cho draft dựa trên dữ liệu hiện tại
     * (Gọi sau mỗi step để tự động tính % và lưu lại cache)
     */
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
        reportCacheService.saveDraftReport(draft, userAccountId, reportId);
        log.info("Updated completion for report {} (user {}): {}%", reportId, userAccountId, percentage);
        return draft;
    }

    @Override
    @Transactional
    public InspectionRemedyResponse updateInspectionRemedyReport(UUID reportId,
                                                                 UpdateInspectionRemedyReportRequest request) {
        // Validate report tồn tại
        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05", reportId,
                        ReportError.REPORT_NOT_FOUND));

        // Validate request không null và trường chính không null (nếu cần, nhưng @Size
        // đã handle length)
        if (request == null) {
            throw exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest",
                    "request", null,
                    ReportError.INVALID_REQUEST);
        }
        if (Objects.isNull(request.getInspectionRemedyReport())) {
            throw exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest",
                    "inspectionRemedyReport", null, ReportError.FIELD_REQUIRED);
        }

        // Update trường
        report.setInspectionRemedyReport(request.getInspectionRemedyReport());
        report.setUpdatedAt(LocalDateTime.now()); // Cập nhật timestamp nếu cần

        ReportA05 saved = reportA05Repository.save(report);

        // Log
        log.info("Updated inspection remedy report for reportId: {}", reportId);

        // Build và return response mới đơn giản
        return InspectionRemedyResponse.builder()
                .reportId(saved.getReportId())
                .inspectionRemedyReport(saved.getInspectionRemedyReport())
                .updatedAt(saved.getUpdatedAt())
                .build();
    }

    private int calculateCompletionPercentage(ReportA05DraftDTO draft) {
        if (draft.getWasteWaterData() != null
                && draft.getWasteManagementData() != null
                && draft.getAirEmissionData() != null) {
            return 100;
        }
        return 0;
    }

    private boolean isSectionComplete(Object sectionDto) {
        if (sectionDto == null)
            return false;

        return true;
    }

    private boolean isDraftComplete(ReportA05DraftDTO draftData) {
        return draftData.getWasteWaterData() != null
                && draftData.getWasteManagementData() != null
                && draftData.getAirEmissionData() != null;
    }

    private void saveOrUpdateWasteWaterData(ReportA05 report, ReportA05DraftDTO draftData) {
        WasteWaterDataDTO dto = draftData.getWasteWaterData(); // Response DTO từ draft
        if (dto == null)
            return;

        WasteWaterData entity;
        if (report.getWasteWaterData() != null && report.getWasteWaterData().getWwId() != null) {
            // Update: Merge partial từ DTO vào entity hiện có
            entity = report.getWasteWaterData();
            wasteWaterDataMapper.updateEntityFromDto(dto, entity); // Method cho WasteWaterDataDTO
        } else {
            // Create: Chuyển từ response DTO sang entity mới
            entity = wasteWaterDataMapper.dtoToEntity(dto); // Đúng method: DTO → entity
            entity.setReport(report);
            // @AfterMapping trong mapper sẽ handle null lists và parent references
        }
        report.setWasteWaterData(entity);
        // Sau đó save entity qua repo nếu cần
    }

    private void saveOrUpdateWasteManagementData(ReportA05 report, ReportA05DraftDTO draftData) {
        WasteManagementDataDTO dto = draftData.getWasteManagementData(); // Response DTO từ draft
        if (dto == null)
            return;

        WasteManagementData entity;
        if (report.getWasteManagementData() != null && report.getWasteManagementData().getWmId() != null) {
            // Update: Merge partial từ DTO vào entity hiện có
            entity = report.getWasteManagementData();
            wasteManagementDataMapper.updateEntityFromDto(dto, entity); // Method cho WasteManagementDataDTO
        } else {
            // Create: Chuyển từ response DTO sang entity mới
            entity = wasteManagementDataMapper.dtoToEntity(dto); // Đúng method: DTO → entity
            entity.setReport(report);
            // @AfterMapping trong mapper sẽ handle null lists và parent references
        }
        report.setWasteManagementData(entity);
        // Sau đó save entity qua repo nếu cần (ví dụ:
        // wasteManagementDataRepository.save(entity))
    }

    private void saveOrUpdateAirEmissionData(ReportA05 report, ReportA05DraftDTO draftData) {
        AirEmissionDataDTO dto = draftData.getAirEmissionData(); // Đây là response DTO từ draft
        if (dto == null)
            return;

        AirEmissionData entity;
        if (report.getAirEmissionData() != null && report.getAirEmissionData().getAirEmissionDataId() != null) {
            // Update: Merge partial từ DTO vào entity hiện có
            entity = report.getAirEmissionData();
            airEmissionDataMapper.updateEntityFromDto(dto, entity); // Method mới cho partial update từ DTO
        } else {
            // Create: Chuyển từ response DTO sang entity mới (KHÔNG dùng toDto!)

            entity = airEmissionDataMapper.dtoToEntity(dto); // Đúng method: DTO → entity

            entity.setReport(report);
            // @AfterMapping trong mapper sẽ handle null lists nếu cần
        }
        report.setAirEmissionData(entity);
        // Sau đó save entity qua repo nếu chưa (ví dụ:
        // airEmissionDataRepository.save(entity))
    }

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
