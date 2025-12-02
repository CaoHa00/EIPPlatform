package com.EIPplatform.service.report.report06;

import com.EIPplatform.model.dto.report.report06.CreateReportRequest;
import com.EIPplatform.model.dto.report.report06.Report06DTO;
import com.EIPplatform.model.dto.report.report06.Report06DraftDTO;

import java.util.UUID;

public interface Report06Service {
    Report06DTO createReport(CreateReportRequest request);

    Report06DTO getReportById(UUID report06Id);

    Report06DraftDTO getDraftData(UUID report06Id, UUID userAccountId);

    Report06DTO submitDraftToDatabase(UUID report06Id, UUID userAccountId);

}