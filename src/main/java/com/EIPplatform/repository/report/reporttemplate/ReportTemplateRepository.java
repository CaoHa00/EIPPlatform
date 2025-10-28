package com.EIPplatform.repository.report.reporttemplate;

import com.EIPplatform.model.entity.report.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Integer> {
    Optional<ReportTemplate> findByTemplateCode(String templateCode);
    List<ReportTemplate> findByIsActiveTrue();
}