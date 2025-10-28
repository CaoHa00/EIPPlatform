package com.EIPplatform.repository.report;

import com.EIPplatform.model.entity.report.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Integer> {
    List<ReportType> findByDueDateAfter(LocalDate dueDate);
    ReportType findByReportName(String reportName);
}