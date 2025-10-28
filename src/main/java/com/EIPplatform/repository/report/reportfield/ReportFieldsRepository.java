package com.EIPplatform.repository.report.reportfield;

import com.EIPplatform.model.entity.report.ReportFields;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportFieldsRepository extends JpaRepository<ReportFields, Integer> {
    List<ReportFields> findByReportId(UUID reportId);
    List<ReportFields> findByReportIdAndFieldName(UUID reportId, String fieldName);
}