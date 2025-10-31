package com.EIPplatform.service.report;

import java.util.UUID;

import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportA05DTO;
import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;
import com.EIPplatform.model.dto.report.report.WasteWaterDataDTO;
import com.EIPplatform.model.entity.report.ReportA05;

public interface ReportA05Service {

    ReportA05DTO createReport(CreateReportRequest request);

    ReportA05DTO getReportById(UUID reportId);

    /**
     * Lưu waste water data vào CACHE (draft)
     */
    ReportA05DraftDTO saveDraftWasteWaterData(UUID reportId, ReportA05DraftDTO data);
    
    /**
     * Lấy waste water data từ CACHE
     */
    ReportA05DraftDTO getDraftData(UUID reportId);
    
    /**
     * Lưu từ CACHE xuống DATABASE
     */
    // ReportA05DraftDTO saveWasteWaterDataToDatabase(UUID reportId);
    
    /**
     * Xóa draft trong cache
     */
    // void deleteDraftWasteWaterData(UUID reportId);

}