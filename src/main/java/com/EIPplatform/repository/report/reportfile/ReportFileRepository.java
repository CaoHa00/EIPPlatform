package com.EIPplatform.repository.report.reportfile;

import com.EIPplatform.model.entity.report.ReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportFileRepository extends JpaRepository<ReportFile, Integer> {

    @Query("SELECT rf FROM ReportFile rf WHERE rf.report.reportId = :reportId")
    List<ReportFile> findByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT rf FROM ReportFile rf WHERE rf.report.reportId = :reportId AND rf.fileName = :fileName")
    ReportFile findByReportIdAndFileName(@Param("reportId") UUID reportId, @Param("fileName") String fileName);
}