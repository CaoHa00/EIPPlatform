package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.airemmissionmanagement.AirEmissionDataMapper;
import com.EIPplatform.mapper.report.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.mapper.report.wastewatermanager.WasteWaterDataMapper;
import com.EIPplatform.model.dto.report.report.*;
import com.EIPplatform.model.dto.report.airemmissionmanagement.airemissiondata.AirEmissionDataDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.dto.report.wastewatermanager.wastewatermanagement.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.model.entity.report.airemmissionmanagement.AirEmissionData;
import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import com.EIPplatform.model.entity.report.wastewatermanager.WasteWaterData;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.service.report.reportcache.ReportCacheService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated // Để enable method-level validation nếu cần
public class ReportA05ServiceImpl implements ReportA05Service {

    ReportA05Repository reportA05Repository;
    BusinessDetailRepository businessDetailRepository;
    ReportCacheService reportCacheService;
    WasteManagementDataMapper wasteManagementDataMapper;
    AirEmissionDataMapper airEmissionDataMapper;
    WasteWaterDataMapper wasteWaterDataMapper;
    ExceptionFactory exceptionFactory;

    @Override
    @Transactional
    public ReportA05DTO createReport(CreateReportRequest request) {
        // 1. Kiểm tra business (CHỈ KHI CÓ businessDetailId)
        BusinessDetail businessDetail = null;
        if (request.getBusinessDetailId() != null) {
            businessDetail = businessDetailRepository
                    .findById(request.getBusinessDetailId())
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail", request.getBusinessDetailId(), ReportError.BUSINESS_NOT_FOUND));
        }

        String reportCode = "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ReportA05 report = ReportA05.builder()
                .reportCode(reportCode)
                .businessDetail(businessDetail)
                .reportYear(request.getReportYear())
                .reportingPeriod(request.getReportingPeriod())
                .version(1)
                .isDeleted(false)
                .completionPercentage(BigDecimal.ZERO)
                .build();

        ReportA05 saved = reportA05Repository.save(report);

        return ReportA05DTO.builder()
                .reportId(saved.getReportId())
                .reportCode(saved.getReportCode())
                .businessDetailId(businessDetail != null ? businessDetail.getBusinessDetailId() : null)
                .companyName(businessDetail != null ? businessDetail.getCompanyName() : null)
                .reportYear(saved.getReportYear())
                .reportingPeriod(saved.getReportingPeriod())
                .completionPercentage(saved.getCompletionPercentage())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public ReportA05DTO getReportById(UUID reportId) {
        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05", reportId, ReportError.REPORT_NOT_FOUND));
        return ReportA05DTO.builder()
                .reportId(report.getReportId())
                .reportCode(report.getReportCode())
                .businessDetailId(report.getBusinessDetail() != null ? report.getBusinessDetail().getBusinessDetailId() : null)
                .companyName(report.getBusinessDetail() != null ? report.getBusinessDetail().getCompanyName() : null)
                .reportYear(report.getReportYear())
                .reportingPeriod(report.getReportingPeriod())
                .reviewNotes(report.getReviewNotes())
                .inspectionRemedyReport(report.getInspectionRemedyReport())
                .completionPercentage(report.getCompletionPercentage())
                .createdAt(report.getCreatedAt())
                .build();
    }

    @Override
    public ReportA05DraftDTO getDraftData(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null) {
            return null;
        }
        return draft;
    }

    /**
     * Cập nhật completion percentage cho draft dựa trên dữ liệu hiện tại
     * (Gọi sau mỗi step để tự động tính % và lưu lại cache)
     */
    @Override
    @Transactional
    public ReportA05DraftDTO updateDraftCompletion(UUID reportId) {
        ReportA05DraftDTO draft = getDraftData(reportId);
        if (draft == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }
        int percentage = calculateCompletionPercentage(draft);
        draft.setCompletionPercentage(percentage);
        draft.setLastModified(LocalDateTime.now());
        reportCacheService.saveDraftReport(draft);
        log.info("Updated completion for report {}: {}%", reportId, percentage);
        return draft;
    }

    @Override
    @Transactional
    public ReportA05DTO submitDraftToDatabase(UUID reportId) {
        ReportA05DraftDTO draftData = getDraftData(reportId);
        if (draftData == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }

        if (draftData.getCompletionPercentage() == null) {
            int percentage = calculateCompletionPercentage(draftData);
            draftData.setCompletionPercentage(percentage);
        }

        if (!isDraftComplete(draftData)) {
            throw exceptionFactory.createValidationException("ReportA05Draft", "completionPercentage",
                    (draftData.getCompletionPercentage() != null ? draftData.getCompletionPercentage() : 0),
                    ReportError.DRAFT_INCOMPLETE);
        }

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

        saveOrUpdateWasteWaterData(report, draftData);
        saveOrUpdateWasteManagementData(report, draftData);
        saveOrUpdateAirEmissionData(report, draftData);

        if (draftData.getCompletionPercentage() != null) {
            report.setCompletionPercentage(BigDecimal.valueOf(draftData.getCompletionPercentage()));
        }

        ReportA05 saved = reportA05Repository.save(report);

        draftData.setIsDraft(false);
        draftData.setLastModified(LocalDateTime.now());
        reportCacheService.deleteDraftReport(reportId);

        BusinessDetail bd = saved.getBusinessDetail();
        return ReportA05DTO.builder()
                .reportCode(saved.getReportCode())
                .businessDetailId(bd != null ? bd.getBusinessDetailId() : null)
                .companyName(bd != null ? bd.getCompanyName() : null)
                .reportYear(saved.getReportYear())
                .reportingPeriod(saved.getReportingPeriod())
                .reviewNotes(saved.getReviewNotes())
                .inspectionRemedyReport(saved.getInspectionRemedyReport())
                .completionPercentage(saved.getCompletionPercentage())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public InspectionRemedyResponse updateInspectionRemedyReport(UUID reportId, @Valid UpdateInspectionRemedyReportRequest request) {
        // Validate report tồn tại
        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

        // Validate request không null và trường chính không null (nếu cần, nhưng @Size đã handle length)
        if (request == null) {
            throw exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest", "request", null, ReportError.INVALID_REQUEST);
        }
        if (Objects.isNull(request.getInspectionRemedyReport())) {
            throw exceptionFactory.createValidationException("UpdateInspectionRemedyReportRequest", "inspectionRemedyReport", null, ReportError.FIELD_REQUIRED);
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
        int score = 0;
        if (isSectionComplete(draft.getWasteWaterData())) score += 33;
        if (isSectionComplete(draft.getWasteManagementData())) score += 33;
        if (isSectionComplete(draft.getAirEmissionData())) score += 34;
        return score;
    }

    private boolean isSectionComplete(Object sectionDto) {
        if (sectionDto == null) return false;

        return true;
    }

    private boolean isDraftComplete(ReportA05DraftDTO draftData) {
        // Tính % nếu null
        if (draftData.getCompletionPercentage() == null) {
            draftData.setCompletionPercentage(calculateCompletionPercentage(draftData));
        }
        boolean allFieldsFilled = draftData.getWasteWaterData() != null
                && draftData.getWasteManagementData() != null
                && draftData.getAirEmissionData() != null;
        boolean completionComplete = draftData.getCompletionPercentage() == 100;
        return allFieldsFilled && completionComplete;
    }

    private void saveOrUpdateWasteWaterData(ReportA05 report, ReportA05DraftDTO draftData) {
        WasteWaterDataDTO dto = draftData.getWasteWaterData();  // Response DTO từ draft
        if (dto == null) return;

        WasteWaterData entity;
        if (report.getWasteWaterData() != null && report.getWasteWaterData().getWwId() != null) {
            // Update: Merge partial từ DTO vào entity hiện có
            entity = report.getWasteWaterData();
            wasteWaterDataMapper.updateEntityFromDto(dto, entity);  // ✅ Method cho WasteWaterDataDTO
        } else {
            // Create: Chuyển từ response DTO sang entity mới
            entity = wasteWaterDataMapper.dtoToEntity(dto);  // ✅ Đúng method: DTO → entity
            entity.setReport(report);
            // @AfterMapping trong mapper sẽ handle null lists và parent references
        }
        report.setWasteWaterData(entity);
        // Sau đó save entity qua repo nếu cần
    }

    private void saveOrUpdateWasteManagementData(ReportA05 report, ReportA05DraftDTO draftData) {
        WasteManagementDataDTO dto = draftData.getWasteManagementData();  // Response DTO từ draft
        if (dto == null) return;

        WasteManagementData entity;
        if (report.getWasteManagementData() != null && report.getWasteManagementData().getWmId() != null) {
            // Update: Merge partial từ DTO vào entity hiện có
            entity = report.getWasteManagementData();
            wasteManagementDataMapper.updateEntityFromDto(dto, entity);  // ✅ Method cho WasteManagementDataDTO
        } else {
            // Create: Chuyển từ response DTO sang entity mới
            entity = wasteManagementDataMapper.dtoToEntity(dto);  // ✅ Đúng method: DTO → entity
            entity.setReport(report);
            // @AfterMapping trong mapper sẽ handle null lists và parent references
        }
        report.setWasteManagementData(entity);
        // Sau đó save entity qua repo nếu cần (ví dụ: wasteManagementDataRepository.save(entity))
    }

    private void saveOrUpdateAirEmissionData(ReportA05 report, ReportA05DraftDTO draftData) {
        AirEmissionDataDTO dto = draftData.getAirEmissionData();  // Đây là response DTO từ draft
        if (dto == null) return;

        AirEmissionData entity;
        if (report.getAirEmissionData() != null && report.getAirEmissionData().getAirEmissionDataId() != null) {
            // Update: Merge partial từ DTO vào entity hiện có
            entity = report.getAirEmissionData();
            airEmissionDataMapper.updateEntityFromDto(dto, entity);  // Method mới cho partial update từ DTO
        } else {
            // Create: Chuyển từ response DTO sang entity mới (KHÔNG dùng toDto!)
            entity = airEmissionDataMapper.dtoToEntity(dto);  // ✅ Đúng method: DTO → entity
            entity.setReport(report);
            // @AfterMapping trong mapper sẽ handle null lists nếu cần
        }
        report.setAirEmissionData(entity);
        // Sau đó save entity qua repo nếu chưa (ví dụ: airEmissionDataRepository.save(entity))
    }
}