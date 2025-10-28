package com.EIPplatform.repository.report.reportsection;

import com.EIPplatform.model.entity.report.ReportSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportSectionRepository extends JpaRepository<ReportSection, UUID> {

    @Query("SELECT rs FROM ReportSection rs WHERE rs.report.reportId = :reportId")
    List<ReportSection> findByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT rs FROM ReportSection rs WHERE rs.report.reportId = :reportId AND rs.sectionType = :sectionType")
    List<ReportSection> findByReportIdAndSectionType(@Param("reportId") UUID reportId, @Param("sectionType") String sectionType);
}