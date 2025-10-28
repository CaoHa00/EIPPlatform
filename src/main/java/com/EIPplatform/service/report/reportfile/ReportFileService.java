package com.EIPplatform.service.report.reportfile;

import com.EIPplatform.model.dto.report.reportfile.ReportFileDTO;
import com.EIPplatform.model.dto.report.reportfile.ReportFileRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportFileService {
    List<ReportFileDTO> findAll();

    Optional<ReportFileDTO> findById(Integer id);

    List<ReportFileDTO> findByReportId(UUID reportId);

    Optional<ReportFileDTO> findByReportIdAndFileName(UUID reportId, String fileName);

    ReportFileDTO createReportFile(ReportFileRequest request, UUID reportId);

    ReportFileDTO updateReportFile(Integer id, ReportFileRequest request);

    void deleteById(Integer id);
}