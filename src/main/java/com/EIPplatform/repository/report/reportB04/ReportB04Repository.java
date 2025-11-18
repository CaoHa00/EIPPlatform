//package com.EIPplatform.repository.report.reportB04;
//
//import com.EIPplatform.model.entity.report.reportB04.ReportB04;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//import java.util.UUID;
//
//
//@Repository
//public interface ReportB04Repository extends JpaRepository<ReportB04, UUID> {
//
//    @Query("SELECT r FROM ReportB05 r " +
//            "LEFT JOIN FETCH r.businessDetail " +
//            "WHERE r.reportId = :reportId")
//    Optional<ReportB04> findByReportIdWithBasic(@Param("reportId") UUID reportId);
//}
