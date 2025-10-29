package com.EIPplatform.service.report;

public interface Report_05Service {

    // ===== Basic CRUD =====

    /**
     *  CHANGED: userId là người tạo, không phải owner
     * businessDetailId trong request là owner thực sự
     */
    // ReportDTO createReport(CreateReportRequest request);

    /**
     *  CHANGED: Validate business ownership thay vì user ownership
     */
    // ReportDTO updateReport(UUID reportId, UpdateReportRequest request);

    // ReportDTO getReportById(UUID reportId);
    // ReportDTO getReportByIdWithDetails(UUID reportId);

    /**
     *  CHANGED: Chỉ owner của business mới xóa được
     */
    // void deleteReport(UUID reportId);

    // ===== List & Filter =====

    /**
     *  CHANGED: Lấy báo cáo theo businessDetailId thay vì userId
     */
    // List<ReportDTO> getReportsByBusiness(UUID businessDetailId, ReportFilterRequest filter);

    /**
     * Admin/Reviewer lấy tất cả
     */
    // List<ReportDTO> getAllReports(ReportFilterRequest filter);

    // ===== Workflow =====
    // void submitReport(SubmitReportRequest request);
    // void reviewReport(ReviewReportRequest request);
    // ReportDTO createNewVersion(UUID reportId);

    // ===== Analytics =====
    // BigDecimal calculateCompletionPercentage(UUID reportId);
    // List<ReportDTO> getReportVersionHistory(UUID reportId);
    // Resource exportReport(UUID reportId, String format);

    /**
     *  CHANGED: Statistics theo business
     */
    // ReportStatisticsDTO getReportStatistics(UUID businessDetailId);

    /**
     *  CHANGED: Upcoming reports theo business
     */
    // List<ReportDTO> getUpcomingReports(UUID businessDetailId, int daysThreshold);

    // // ===== File Management =====
    // ReportFileDTO uploadFile(UUID reportId, MultipartFile file);
    // void deleteFile(Integer fileId);
    // Resource downloadFile(UUID reportId, Integer fileId);
    // List<ReportFileDTO> getFilesByReport(UUID reportId);
}