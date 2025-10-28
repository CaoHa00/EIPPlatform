package com.EIPplatform.repository.report.reportsection;

import com.EIPplatform.model.entity.report.ReportSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportSectionRepository extends JpaRepository<ReportSection, UUID> {
    List<ReportSection> findByReportId(UUID reportId);
    List<ReportSection> findByReportIdAndSectionType(UUID reportId, String sectionType);
}