package com.EIPplatform.service.report.reporta05;

import java.util.UUID;

import com.EIPplatform.model.dto.report.report.CreateReportRequest;
import com.EIPplatform.model.dto.report.report.ReportA05DTO;
import com.EIPplatform.model.dto.report.report.ReportA05DraftDTO;

public interface ReportA05Service {

    ReportA05DTO createReport(CreateReportRequest request);

    ReportA05DTO getReportById(UUID reportId);

    /**
     * Lấy draft data từ CACHE
     */
    ReportA05DraftDTO getDraftData(UUID reportId);

    /**
     * Submit draft từ CACHE xuống DATABASE
     */
    ReportA05DTO submitDraftToDatabase(UUID reportId);

}