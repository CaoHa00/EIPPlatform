package com.EIPplatform.repository.report.reportB04.part1;

import com.EIPplatform.model.entity.report.reportB04.part01.ReportInvestorDetail;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInvestorDetailRepository extends JpaRepository<ReportInvestorDetail, Long>, JpaSpecificationExecutor<ReportInvestorDetail> {
    @Query("SELECT w FROM ReportInvestorDetail w WHERE w.report.reportId = :reportId")
    Optional<ReportInvestorDetail> findByReportIdWithCollections(@Param("reportId") UUID reportId);
}