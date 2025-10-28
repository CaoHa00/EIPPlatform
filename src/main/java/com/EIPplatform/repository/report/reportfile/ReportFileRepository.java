package com.EIPplatform.repository.report.reportfile;

import com.EIPplatform.model.entity.report.ReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportFileRepository extends JpaRepository<ReportFile, Integer> {
    List<ReportFile> findByReportId(UUID reportId);
    ReportFile findByReportIdAndFileName(UUID reportId, String fileName);
}