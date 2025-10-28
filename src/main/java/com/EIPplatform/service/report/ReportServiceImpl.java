package com.EIPplatform.service.report;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.mapper.report.ReportMapper;
import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportDTO;
import com.EIPplatform.model.dto.report.reportfield.ReportFilterRequest;
import com.EIPplatform.model.dto.report.report.UpdateReportRequest;
import com.EIPplatform.model.dto.report.reportfile.ReportFileDTO;
import com.EIPplatform.model.dto.report.statistics.ReportStatisticsDTO;
import com.EIPplatform.model.dto.report.workflow.ReviewReportRequest;
import com.EIPplatform.model.dto.report.workflow.SubmitReportRequest;
import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.report.ReportStatus;
import com.EIPplatform.model.entity.report.ReportType;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.entity.user.userInformation.BusinessDetail;
import com.EIPplatform.repository.report.ReportRepository;
import com.EIPplatform.repository.report.reportstatus.ReportStatusRepository;
import com.EIPplatform.repository.report.ReportTypeRepository;
import com.EIPplatform.exception.errorCategories.ReportError;
import com.EIPplatform.repository.user.BussinessDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportStatusRepository reportStatusRepository;
    private final BussinessDetailRepository businessDetailRepository;
    private final ReportMapper reportMapper;
    private final ExceptionFactory exceptionFactory;

    private UserAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
        }
        UserAccount currentUser = (UserAccount) authentication.getPrincipal();
        if (currentUser == null) {
            throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
        }
        return currentUser;
    }

    @Override
    public ReportDTO createReport(CreateReportRequest request) {
        BusinessDetail businessDetail = businessDetailRepository.findById(request.getBusinessDetailId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail", "BusinessDetailId", request.getBusinessDetailId(), ReportError.BUSINESS_DETAIL_NOT_FOUND));

        UserAccount userAccount = getCurrentUser();

        validateUserCanAccessBusiness(userAccount, businessDetail);

        ReportType reportType = reportTypeRepository.findById(request.getReportTypeId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportType", "id", request.getReportTypeId(), ReportError.REPORT_TYPE_NOT_FOUND));

        if (reportRepository.existsByBusinessDetailAndReportTypeAndReportYearAndReportingPeriod(
                businessDetail, reportType, request.getReportYear(), request.getReportingPeriod())) {
            throw exceptionFactory.createAlreadyExistsException("Report", "businessDetailAndReportTypeAndReportYearAndReportingPeriod",
                    Map.of("businessId", request.getBusinessDetailId(), "typeId", request.getReportTypeId(), "year", request.getReportYear(), "period", request.getReportingPeriod()),
                    ReportError.REPORT_DUPLICATE);
        }

        ReportStatus draftStatus = reportStatusRepository.findByStatusCode("DRAFT")
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportStatus", "code", "DRAFT", ReportError.REPORT_STATUS_NOT_FOUND));

        String reportCode = generateReportCode(reportType, request.getReportYear(),
                request.getReportingPeriod(), businessDetail.getBussinessDetailId());

        Report report = reportMapper.toEntity(request, businessDetail, reportType, draftStatus, reportCode);
        report = reportRepository.save(report);
        log.info("Report created successfully: {}", reportCode);
        return reportMapper.toDTO(report);
    }

    @Override
    public ReportDTO updateReport(UUID reportId, UpdateReportRequest request) {
        UserAccount currentUser = getCurrentUser();

        Report report = validateBusinessOwnership(reportId, currentUser);
        validateReportEditable(report);

        if (request.getReportingPeriod() != null) {
            report.setReportingPeriod(request.getReportingPeriod());
        }

        report.setUpdatedAt(LocalDateTime.now());
        report = reportRepository.save(report);
        log.info("Report updated successfully: {}", reportId);
        return reportMapper.toDTO(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportsByBusiness(UUID businessDetailId, ReportFilterRequest filter) {
        UserAccount currentUser = getCurrentUser();

        BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail", "BusinessDetailId", businessDetailId, ReportError.BUSINESS_DETAIL_NOT_FOUND));

        validateUserCanAccessBusiness(currentUser, businessDetail);

        List<Report> reports = reportRepository.findWithFilters(
                businessDetailId,
                filter.getReportYear(),
                filter.getReportTypeId(),
                filter.getStatusId(),
                filter.getSubmittedById()
        );

        return reports.stream()
                .map(reportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void submitReport(SubmitReportRequest request) {
        UserAccount currentUser = getCurrentUser();

        Report report = validateBusinessOwnership(request.getReportId(), currentUser);

        String statusCode = report.getReportStatus().getStatusCode();
        if (!"DRAFT".equals(statusCode) && !"REJECTED".equals(statusCode)) {
            throw exceptionFactory.createCustomException(ReportError.INVALID_REPORT_OPERATION);
        }

        BigDecimal completion = calculateCompletionPercentage(request.getReportId());
        if (completion.compareTo(new BigDecimal("80")) < 0) {
            throw exceptionFactory.createCustomException(List.of("completionPercentage"), List.of(completion), ReportError.INVALID_REPORT_OPERATION);
        }

        ReportStatus pendingStatus = reportStatusRepository.findByStatusCode("PENDING")
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportStatus", "code", "PENDING", ReportError.REPORT_STATUS_NOT_FOUND));

        report.setReportStatus(pendingStatus);
        report.setSubmittedBy(currentUser);
        report.setSubmittedAt(LocalDateTime.now());
        report.setCompletionPercentage(completion);
        report.setUpdatedAt(LocalDateTime.now());

        reportRepository.save(report);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportStatisticsDTO getReportStatistics(UUID businessDetailId) {
        UserAccount currentUser = getCurrentUser();

        BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail", "BusinessDetailId", businessDetailId, ReportError.BUSINESS_DETAIL_NOT_FOUND));

        validateUserCanAccessBusiness(currentUser, businessDetail);

        Long totalReports = reportRepository.countByBusinessAndStatus(businessDetailId, "DRAFT") +
                reportRepository.countByBusinessAndStatus(businessDetailId, "PENDING") +
                reportRepository.countByBusinessAndStatus(businessDetailId, "APPROVED") +
                reportRepository.countByBusinessAndStatus(businessDetailId, "REJECTED");

        Long draftReports = reportRepository.countByBusinessAndStatus(businessDetailId, "DRAFT");
        Long pendingReports = reportRepository.countByBusinessAndStatus(businessDetailId, "PENDING");
        Long approvedReports = reportRepository.countByBusinessAndStatus(businessDetailId, "APPROVED");
        Long rejectedReports = reportRepository.countByBusinessAndStatus(businessDetailId, "REJECTED");

        BigDecimal avgCompletion = reportRepository.getAverageCompletionPercentageByBusiness(businessDetailId);

        return ReportStatisticsDTO.builder()
                .totalReports(totalReports)
                .draftReports(draftReports)
                .pendingReports(pendingReports)
                .approvedReports(approvedReports)
                .rejectedReports(rejectedReports)
                .averageCompletionPercentage(avgCompletion != null ? avgCompletion : BigDecimal.ZERO)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getUpcomingReports(UUID businessDetailId, int daysThreshold) {
        UserAccount currentUser = getCurrentUser();

        BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail", "BusinessDetailId", businessDetailId, ReportError.BUSINESS_DETAIL_NOT_FOUND));

        validateUserCanAccessBusiness(currentUser, businessDetail);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysThreshold);

        List<Report> reports = reportRepository.findUpcomingReports(businessDetailId, startDate, endDate);

        return reports.stream()
                .map(reportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateCompletionPercentage(UUID reportId) {
        UserAccount currentUser = getCurrentUser();

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));

        validateUserCanAccessBusiness(currentUser, report.getBusinessDetail());

        int totalSections = 0;
        int completedSections = 0;

        if (report.getReportFields() != null && !report.getReportFields().isEmpty()) {
            totalSections += 10;
            completedSections += (int) report.getReportFields().stream()
                    .filter(f -> f.getFieldValue() != null && !f.getFieldValue().trim().isEmpty())
                    .count();
        }

        if (totalSections == 0) return BigDecimal.ZERO;

        return BigDecimal.valueOf(completedSections)
                .divide(BigDecimal.valueOf(totalSections), 2, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    // ========== PRIVATE HELPER METHODS ==========

    private void validateUserCanAccessBusiness(UserAccount userAccount, BusinessDetail businessDetail) {
        if (userAccount.getBusinessDetail() != null) {
            if (!userAccount.getBusinessDetail().getBussinessDetailId().equals(businessDetail.getBussinessDetailId())) {
                throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
            }
        }
        if (userAccount.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
            return;
        }
    }

    private Report validateBusinessOwnership(UUID reportId, UserAccount currentUser) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));

        validateUserCanAccessBusiness(currentUser, report.getBusinessDetail());

        return report;
    }

    private void validateReportEditable(Report report) {
        String statusCode = report.getReportStatus().getStatusCode();
        if (!"DRAFT".equals(statusCode) && !"REJECTED".equals(statusCode)) {
            throw exceptionFactory.createCustomException(List.of("currentStatus"), List.of(statusCode), ReportError.INVALID_REPORT_OPERATION);
        }
    }

    private String generateReportCode(ReportType reportType, Integer year, String period, UUID businessDetailId) {
        String typeCode = reportType.getReportName()
                .replaceAll("[^A-Z0-9]", "")
                .toUpperCase();
        if (typeCode.length() > 5) {
            typeCode = typeCode.substring(0, 5);
        }

        String periodCode = period != null ? period.replaceAll("[^A-Z0-9]", "") : "ALL";
        String businessCode = businessDetailId.toString().substring(0, 8);
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);

        return String.format("%s_Y%d_P%s_%s_%s", typeCode, year, periodCode, businessCode, timestamp);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReportById(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        Report report = validateBusinessOwnership(reportId, currentUser);
        return reportMapper.toDTO(report);
    }

    @Override
    @Transactional(readOnly = true)
    public ReportDTO getReportByIdWithDetails(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        Report report = validateBusinessOwnership(reportId, currentUser);
        return reportMapper.toFullDTO(report);
    }

    @Override
    public void deleteReport(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        Report report = validateBusinessOwnership(reportId, currentUser);

        if (!"DRAFT".equals(report.getReportStatus().getStatusCode())) {
            throw exceptionFactory.createCustomException(List.of("currentStatus"), List.of(report.getReportStatus().getStatusCode()), ReportError.INVALID_REPORT_OPERATION);
        }

        report.setIsDeleted(true);
        report.setDeletedAt(LocalDateTime.now());
        reportRepository.save(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getAllReports(ReportFilterRequest filter) {
        UserAccount currentUser = getCurrentUser();

        UUID businessDetailId = filter.getBusinessDetailId();
        if (businessDetailId != null) {
            BusinessDetail businessDetail = businessDetailRepository.findById(businessDetailId)
                    .orElseThrow(() -> exceptionFactory.createNotFoundException("BusinessDetail", "BusinessDetailId", businessDetailId, ReportError.BUSINESS_DETAIL_NOT_FOUND));
            validateUserCanAccessBusiness(currentUser, businessDetail);
        } else {
            if (!currentUser.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
                throw exceptionFactory.createCustomException(ReportError.FORBIDDEN_BUSINESS_ACCESS);
            }
        }

        List<Report> reports = reportRepository.findWithFilters(
                businessDetailId,
                filter.getReportYear(),
                filter.getReportTypeId(),
                filter.getStatusId(),
                filter.getSubmittedById()
        );

        return reports.stream()
                .map(reportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void reviewReport(ReviewReportRequest request) {
        UserAccount currentUser = getCurrentUser();

        Report report = reportRepository.findById(request.getReportId())
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", request.getReportId(), ReportError.REPORT_NOT_FOUND));

        validateUserCanAccessBusiness(currentUser, report.getBusinessDetail());

        String targetStatusCode = request.getApproved() ? "APPROVED" : "REJECTED";
        ReportStatus targetStatus = reportStatusRepository.findByStatusCode(targetStatusCode)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportStatus", "code", targetStatusCode, ReportError.REPORT_STATUS_NOT_FOUND));

        report.setReportStatus(targetStatus);
        report.setReviewedBy(currentUser);
        report.setReviewedAt(LocalDateTime.now());
        report.setReviewNotes(request.getReviewNotes());
        report.setUpdatedAt(LocalDateTime.now());

        reportRepository.save(report);

        log.info("Report reviewed: {} by reviewer: {}", request.getReportId(), currentUser.getUserAccountId());
    }

    @Override
    public ReportDTO createNewVersion(UUID reportId) {
        UserAccount currentUser = getCurrentUser();

        Report originalReport = validateBusinessOwnership(reportId, currentUser);

        ReportStatus draftStatus = reportStatusRepository.findByStatusCode("DRAFT")
                .orElseThrow(() -> exceptionFactory.createNotFoundException("ReportStatus", "code", "DRAFT", ReportError.REPORT_STATUS_NOT_FOUND));

        Report newVersion = Report.builder()
                .reportCode(originalReport.getReportCode() + "_v" + (originalReport.getVersion() + 1))
                .businessDetail(originalReport.getBusinessDetail())
                .reportType(originalReport.getReportType())
                .reportYear(originalReport.getReportYear())
                .reportingPeriod(originalReport.getReportingPeriod())
                .reportStatus(draftStatus)
                .completionPercentage(BigDecimal.ZERO)
                .version(originalReport.getVersion() + 1)
                .parentReport(originalReport.getParentReport() != null ? originalReport.getParentReport() : originalReport)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        newVersion = reportRepository.save(newVersion);

        log.info("New version created for report: {}", reportId);

        return reportMapper.toDTO(newVersion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDTO> getReportVersionHistory(UUID reportId) {
        UserAccount currentUser = getCurrentUser();

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("Report", "id", reportId, ReportError.REPORT_NOT_FOUND));

        validateUserCanAccessBusiness(currentUser, report.getBusinessDetail());

        Report parentReport = report.getParentReport() != null ? report.getParentReport() : report;
        List<Report> versions = reportRepository.findByParentReportOrderByVersionDesc(parentReport);

        return versions.stream()
                .map(reportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Resource exportReport(UUID reportId, String format) {
        UserAccount currentUser = getCurrentUser();
        validateBusinessOwnership(reportId, currentUser);
        throw new UnsupportedOperationException("Export not yet implemented");
    }

    @Override
    public ReportFileDTO uploadFile(UUID reportId, MultipartFile file) {
        UserAccount currentUser = getCurrentUser();
        validateBusinessOwnership(reportId, currentUser);
        throw new UnsupportedOperationException("File upload not yet implemented");
    }

    @Override
    public void deleteFile(Integer fileId) {
        getCurrentUser();
        throw new UnsupportedOperationException("File delete not yet implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadFile(UUID reportId, Integer fileId) {
        UserAccount currentUser = getCurrentUser();
        validateBusinessOwnership(reportId, currentUser);
        throw new UnsupportedOperationException("File download not yet implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportFileDTO> getFilesByReport(UUID reportId) {
        UserAccount currentUser = getCurrentUser();
        validateBusinessOwnership(reportId, currentUser);
        throw new UnsupportedOperationException("Get files not yet implemented");
    }
}