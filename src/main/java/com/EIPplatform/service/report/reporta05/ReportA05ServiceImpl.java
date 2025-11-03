package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.mapper.report.wastemanagement.WasteManagementDataMapper;
import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportA05DTO;
import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.wastemanagement.WasteManagementDataDTO;
import com.EIPplatform.model.entity.report.ReportA05;
import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;
import com.EIPplatform.model.entity.user.businessInformation.BusinessDetail;
import com.EIPplatform.repository.report.ReportA05Repository;
import com.EIPplatform.repository.user.BusinessDetailRepository;
import com.EIPplatform.service.report.reportcache.ReportCacheService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportA05ServiceImpl implements ReportA05Service {

    ReportA05Repository reportA05Repository;
    BusinessDetailRepository businessDetailRepository;
    ReportCacheService reportCacheService;
    WasteManagementDataMapper wasteManagementDataMapper;
    ExceptionFactory exceptionFactory;

    // Creates a new report with basic metadata and saves it to the database
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

        // 2. Generate report code đơn giản
        String reportCode = "RPT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 3. Tạo report (businessDetail có thể null)
        ReportA05 report = ReportA05.builder()
                .reportCode(reportCode)
                .businessDetail(businessDetail) // CÓ THỂ NULL
                .reportYear(request.getReportYear())
                .reportingPeriod(request.getReportingPeriod())
                .version(1)
                .isDeleted(false)
                .completionPercentage(BigDecimal.ZERO)
                .build();

        // 4. Lưu vào DB
        ReportA05 saved = reportA05Repository.save(report);

        // 5. Trả về DTO
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

    // Retrieves report details by ID
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
                .completionPercentage(report.getCompletionPercentage())
                .createdAt(report.getCreatedAt())
                .build();
    }

    // Retrieves draft data from cache
    @Override
    public ReportA05DraftDTO getDraftData(UUID reportId) {
        ReportA05DraftDTO draft = reportCacheService.getDraftReport(reportId);
        if (draft == null) {
            return null;
        }
        return draft;
    }

    // Saves complete draft data from cache to database
    @Override
    @Transactional
    public ReportA05DTO submitDraftToDatabase(UUID reportId) {
        ReportA05DraftDTO draftData = getDraftData(reportId);
        if (draftData == null) {
            throw exceptionFactory.createCustomException(ReportError.DRAFT_NOT_FOUND);
        }

        if (!isDraftComplete(draftData)) {
            throw exceptionFactory.createValidationException("ReportA05Draft", "completionPercentage",
                    (draftData.getCompletionPercentage() != null ? draftData.getCompletionPercentage() : 0),
                    ReportError.DRAFT_INCOMPLETE);
        }

        ReportA05 report = reportA05Repository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportA05", reportId, ReportError.REPORT_NOT_FOUND));

        saveOrUpdateWasteManagementData(report, draftData);

        // TODO: Thêm logic cho các phần khác nếu có (e.g., AirEmissionData, SolidWasteData, WasteWaterData - delegated to sub-services)

        // 6. Cập nhật completion percentage cho report (dựa trên draft)
        if (draftData.getCompletionPercentage() != null) {
            report.setCompletionPercentage(BigDecimal.valueOf(draftData.getCompletionPercentage()));
        }

        // 7. Lưu toàn bộ vào DB
        ReportA05 saved = reportA05Repository.save(report);

        // 8. Sau khi lưu đầy đủ, cập nhật draft metadata và xóa cache (hoặc set isDraft = false)
        draftData.setIsDraft(false); // Đánh dấu không còn là draft
        draftData.setLastModified(LocalDateTime.now());
        // Không update cache nữa, thay vào đó xóa draft vì đã submit đầy đủ
        // reportCacheService.deleteDraftReport(reportId);

        BusinessDetail bd = saved.getBusinessDetail();
        return ReportA05DTO.builder()
                .reportId(saved.getReportId())
                .reportCode(saved.getReportCode())
                .businessDetailId(bd != null ? bd.getBusinessDetailId() : null)
                .companyName(bd != null ? bd.getCompanyName() : null)
                .reportYear(saved.getReportYear())
                .reportingPeriod(saved.getReportingPeriod())
                .completionPercentage(saved.getCompletionPercentage())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    // Helper method: Kiểm tra draft có đầy đủ không (dựa trên các field chính và completionPercentage)
    private boolean isDraftComplete(ReportA05DraftDTO draftData) {
        // Kiểm tra các field chính không null
        boolean allFieldsFilled = draftData.getWasteManagementData() != null
                // Thêm các field khác nếu có: && draftData.getAirEmissionData() != null && ...
                ;

        // Kiểm tra completionPercentage == 100 nếu có
        boolean completionComplete = draftData.getCompletionPercentage() != null && draftData.getCompletionPercentage() == 100;

        // Hoặc kiểm tra isDraft == false, nhưng dùng completion để linh hoạt
        return allFieldsFilled && completionComplete;
    }

    // Helper method: Lưu hoặc update WasteManagementData (tương tự)
    private void saveOrUpdateWasteManagementData(ReportA05 report, ReportA05DraftDTO draftData) {
        WasteManagementDataDTO dto = draftData.getWasteManagementData();
        if (dto == null) {
            return;
        }

        WasteManagementData entity = wasteManagementDataMapper.dtoToEntity(dto);
        entity.setReport(report);
        report.setWasteManagementData(entity);
    }

    // @Override
    // public void deleteDraftWasteWaterData(UUID reportId) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteDraftWasteWaterData'");
    // }

}