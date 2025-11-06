package com.EIPplatform.repository.report;

import com.EIPplatform.model.entity.report.ReportA05;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportA05Repository extends JpaRepository<ReportA05, UUID> {
    
//     @Query("""
//         SELECT COUNT(r)
//         FROM ReportA05 r
//         WHERE r.businessDetail.businessDetailId = :businessDetailId
//         AND r.reportYear = :reportYear
//         AND r.isDeleted = false
//     """)
//     long countByBusinessDetailIdAndYear(
//         @Param("businessDetailId") UUID businessDetailId,
//         @Param("reportYear") Integer reportYear
//     );
@Query("SELECT r FROM ReportA05 r " +
        "LEFT JOIN FETCH r.businessDetail " +
        "LEFT JOIN FETCH r.wasteWaterData wwd " +
        "LEFT JOIN FETCH wwd.monitoringExceedances " +
        "LEFT JOIN FETCH wwd.monitoringStats " +
        "LEFT JOIN FETCH wwd.monitoringIncidents " +
        "LEFT JOIN FETCH wwd.qcvnExceedances " +
        "LEFT JOIN FETCH r.wasteManagementData wmd " +
        "LEFT JOIN FETCH wmd.domesticSolidWasteStats " +
        "LEFT JOIN FETCH wmd.industrialSolidWasteStats " +
        "LEFT JOIN FETCH wmd.recycleIndustrialWasteStats " +
        "LEFT JOIN FETCH wmd.otherSolidWasteStats " +
        "LEFT JOIN FETCH wmd.hazardousWasteStats " +
        "LEFT JOIN FETCH wmd.exportedHwStats " +
        "LEFT JOIN FETCH wmd.selfTreatedHwStats " +
        "LEFT JOIN FETCH wmd.popInventoryStats " +
        "LEFT JOIN FETCH r.airEmissionData aed " +
        "LEFT JOIN FETCH aed.airMonitoringExceedances " +
        "LEFT JOIN FETCH aed.airAutoMonitoringStats " +
        "LEFT JOIN FETCH aed.airAutoMonitoringIncidents " +
        "LEFT JOIN FETCH aed.airAutoQcvnExceedances " +
        "WHERE r.reportId = :reportId")
Optional<ReportA05> findByReportIdWithFullDetails(@Param("reportId") UUID reportId);
}