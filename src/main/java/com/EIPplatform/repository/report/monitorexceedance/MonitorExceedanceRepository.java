package com.EIPplatform.repository.report.monitorexceedance;

import com.EIPplatform.model.entity.report.MonitorExceedance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MonitorExceedanceRepository extends JpaRepository<MonitorExceedance, Integer> {

    List<MonitorExceedance> findByReportSectionSectionId(UUID sectionId);

    @Query("SELECT m FROM MonitorExceedance m WHERE m.report.reportId = :reportId")
    List<MonitorExceedance> findByReportId(@Param("reportId") UUID reportId);

    List<MonitorExceedance> findByReportSectionSectionIdAndMonitoringDate(UUID sectionId, LocalDate monitoringDate);
}