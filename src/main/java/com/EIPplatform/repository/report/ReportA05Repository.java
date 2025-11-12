package com.EIPplatform.repository.report;

import com.EIPplatform.model.entity.report.report05.ReportA05;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportA05Repository extends JpaRepository<ReportA05, UUID> {

    @Query("SELECT r FROM ReportA05 r " +
            "LEFT JOIN FETCH r.businessDetail " +
            "WHERE r.reportId = :reportId")
    Optional<ReportA05> findByReportIdWithBasic(@Param("reportId") UUID reportId);
}
