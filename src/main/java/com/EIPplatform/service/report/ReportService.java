package com.EIPplatform.service.report;

import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportDTO;
import com.EIPplatform.model.dto.report.reportfield.ReportFilterRequest;
import com.EIPplatform.model.dto.report.report.UpdateReportRequest;
import com.EIPplatform.model.dto.report.reportfile.ReportFileDTO;
import com.EIPplatform.model.dto.report.statistics.ReportStatisticsDTO;
import com.EIPplatform.model.dto.report.workflow.ReviewReportRequest;
import com.EIPplatform.model.dto.report.workflow.SubmitReportRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ReportService {

    // ===== Basic CRUD =====

    /**
     *  CHANGED: userId là người tạo, không phải owner
     * businessDetailId trong request là owner thực sự
     */
    ReportDTO createReport(CreateReportRequest request);

    /**
     *  CHANGED: Validate business ownership thay vì user ownership
     */
    ReportDTO updateReport(UUID reportId, UpdateReportRequest request);

    ReportDTO getReportById(UUID reportId);
    ReportDTO getReportByIdWithDetails(UUID reportId);

    /**
     *  CHANGED: Chỉ owner của business mới xóa được
     */
    void deleteReport(UUID reportId);

    // ===== List & Filter =====

    /**
     *  CHANGED: Lấy báo cáo theo businessDetailId thay vì userId
     */
    List<ReportDTO> getReportsByBusiness(UUID businessDetailId, ReportFilterRequest filter);

    /**
     * Admin/Reviewer lấy tất cả
     */
    List<ReportDTO> getAllReports(ReportFilterRequest filter);

    // ===== Workflow =====
    void submitReport(SubmitReportRequest request);
    void reviewReport(ReviewReportRequest request);
    ReportDTO createNewVersion(UUID reportId);

    // ===== Analytics =====
    BigDecimal calculateCompletionPercentage(UUID reportId);
    List<ReportDTO> getReportVersionHistory(UUID reportId);
    Resource exportReport(UUID reportId, String format);

    /**
     *  CHANGED: Statistics theo business
     */
    ReportStatisticsDTO getReportStatistics(UUID businessDetailId);

    /**
     *  CHANGED: Upcoming reports theo business
     */
    List<ReportDTO> getUpcomingReports(UUID businessDetailId, int daysThreshold);

    // ===== File Management =====
    ReportFileDTO uploadFile(UUID reportId, MultipartFile file);
    void deleteFile(Integer fileId);
    Resource downloadFile(UUID reportId, Integer fileId);
    List<ReportFileDTO> getFilesByReport(UUID reportId);
}