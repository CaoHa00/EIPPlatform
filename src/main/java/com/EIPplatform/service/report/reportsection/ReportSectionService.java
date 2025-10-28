package com.EIPplatform.service.report.reportsection;

import com.EIPplatform.model.dto.report.reportsection.ReportSectionDTO;
import com.EIPplatform.model.dto.report.reportsection.ReportSectionRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportSectionService {
    List<ReportSectionDTO> findAll();

    Optional<ReportSectionDTO> findById(UUID id);

    List<ReportSectionDTO> findByReportId(UUID reportId);

    List<ReportSectionDTO> findByReportIdAndSectionType(UUID reportId, String sectionType);

    ReportSectionDTO createReportSection(ReportSectionRequest request, UUID reportId);

    ReportSectionDTO updateReportSection(UUID id, ReportSectionRequest request);

    void deleteById(UUID id);
}