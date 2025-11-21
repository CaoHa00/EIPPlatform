package com.EIPplatform.service.report.reportB04;

import java.util.UUID;

import com.EIPplatform.model.dto.report.report05.CreateReportRequest;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DTO;
import com.EIPplatform.model.dto.report.reportB04.ReportB04DraftDTO;
import com.EIPplatform.model.entity.report.reportB04.ReportB04;

public interface  ReportB04Service {
    
    ReportB04DTO createReport(CreateReportRequest request);

    ReportB04DTO getOrCreateReportByBusinessDetailId(UUID businessDetailId);

    /**
     * Lấy draft data từ CACHE
     */
    ReportB04DraftDTO getDraftData(UUID reportId, UUID userAccountId);

    /**
     * Submit draft từ CACHE xuống DATABASE
     */
    ReportB04DTO submitDraftToDatabase(UUID reportId, UUID userAccountId);

    // /**
    //  * Cập nhật completion percentage cho draft dựa trên dữ liệu hiện tại
    //  * (Gọi sau mỗi step để tự động tính % và lưu lại cache)
    //  */
    // ReportA05DraftDTO updateDraftCompletion(UUID reportId, UUID userAccountId);

    // /**
    //  * Cập nhật inspection remedy report cho report (trực tiếp vào database)
    //  */
    // InspectionRemedyResponse updateInspectionRemedyReport(UUID reportId, UpdateInspectionRemedyReportRequest request);

}
