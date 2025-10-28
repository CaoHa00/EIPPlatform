package com.EIPplatform.service.report;

import com.EIPplatform.model.dto.report.reporttype.ReportTypeDTO;
import com.EIPplatform.model.dto.report.reporttype.ReportTypeRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReportTypeService {
    List<ReportTypeDTO> findAll();

    Optional<ReportTypeDTO> findById(Integer id);

    List<ReportTypeDTO> findByDueDateAfter(LocalDate dueDate);

    Optional<ReportTypeDTO> findByReportName(String reportName);

    ReportTypeDTO createReportType(ReportTypeRequest request);

    ReportTypeDTO updateReportType(Integer id, ReportTypeRequest request);

    void deleteById(Integer id);
}