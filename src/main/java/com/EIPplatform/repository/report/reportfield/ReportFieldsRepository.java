package com.EIPplatform.repository.report.reportfield;

import com.EIPplatform.model.entity.report.ReportFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportFieldsRepository extends JpaRepository<ReportFields, Integer> {

    @Query("SELECT rf FROM ReportFields rf WHERE rf.report.reportId = :reportId")
    List<ReportFields> findByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT rf FROM ReportFields rf WHERE rf.report.reportId = :reportId AND rf.fieldName = :fieldName")
    List<ReportFields> findByReportIdAndFieldName(@Param("reportId") UUID reportId, @Param("fieldName") String fieldName);
}