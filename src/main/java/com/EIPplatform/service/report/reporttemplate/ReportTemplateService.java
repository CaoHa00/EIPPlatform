package com.EIPplatform.service.report.reporttemplate;

import com.EIPplatform.model.dto.report.reporttemplate.ReportTemplateDTO;
import com.EIPplatform.model.dto.report.reporttemplate.ReportTemplateRequest;

import java.util.List;
import java.util.Optional;

public interface ReportTemplateService {
    List<ReportTemplateDTO> findAll();

    Optional<ReportTemplateDTO> findById(Integer id);

    Optional<ReportTemplateDTO> findByTemplateCode(String templateCode);

    List<ReportTemplateDTO> findActiveTemplates();

    ReportTemplateDTO createReportTemplate(ReportTemplateRequest request);

    ReportTemplateDTO updateReportTemplate(Integer id, ReportTemplateRequest request);

    void deleteById(Integer id);
}