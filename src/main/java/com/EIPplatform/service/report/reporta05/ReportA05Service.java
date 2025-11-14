package com.EIPplatform.service.report.reporta05;

import com.EIPplatform.model.dto.report.report05.*;

import java.util.UUID;

public interface ReportA05Service {

    ReportA05DTO createReport(CreateReportRequest request);

    ReportA05DTO getReportById(UUID reportId);

    /**
     * Lấy draft data từ CACHE
     */
    ReportA05DraftDTO getDraftData(UUID reportId, UUID userAccountId);

    /**
     * Submit draft từ CACHE xuống DATABASE
     */
    ReportA05DTO submitDraftToDatabase(UUID reportId, UUID userAccountId);

    /**
     * Cập nhật completion percentage cho draft dựa trên dữ liệu hiện tại
     * (Gọi sau mỗi step để tự động tính % và lưu lại cache)
     */
    ReportA05DraftDTO updateDraftCompletion(UUID reportId, UUID userAccountId);

    /**
     * Cập nhật inspection remedy report cho report (trực tiếp vào database)
     */
    InspectionRemedyResponse updateInspectionRemedyReport(UUID reportId, UpdateInspectionRemedyReportRequest request);

    // TODO: Thêm các method delete section nếu cần (ví dụ: xóa draft data của một
    // phần cụ thể)
    // void deleteDraftWasteWaterData(UUID reportId);
    // void deleteDraftWasteManagementData(UUID reportId);
    // void deleteDraftAirEmissionData(UUID reportId);

    // Generate report file
    byte[] generateReportFile(UUID reportId, UUID userAccountId) throws Exception;

}