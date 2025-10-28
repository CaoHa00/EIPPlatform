package com.EIPplatform.service.report.reportfield;

import com.EIPplatform.model.dto.report.reportfield.ReportFieldDTO;
import com.EIPplatform.model.dto.report.reportfield.ReportFieldRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReportFieldsService {
    List<ReportFieldDTO> findAll();

    Optional<ReportFieldDTO> findById(Integer id);

    List<ReportFieldDTO> findByReportId(UUID reportId);

    List<ReportFieldDTO> findByReportIdAndFieldName(UUID reportId, String fieldName);

    ReportFieldDTO createReportField(ReportFieldRequest request, UUID reportId);

    ReportFieldDTO updateReportField(Integer id, ReportFieldRequest request);

    void deleteById(Integer id);
}