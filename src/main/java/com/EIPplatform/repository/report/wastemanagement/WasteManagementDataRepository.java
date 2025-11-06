package com.EIPplatform.repository.report.wastemanagement;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.report.wastemanagement.WasteManagementData;

@Repository
public interface WasteManagementDataRepository extends JpaRepository<WasteManagementData, Long> {

    Optional<WasteManagementData> findByReport_ReportId(UUID reportId);

    boolean existsByReport_ReportId(UUID reportId);

    @Query("SELECT w FROM WasteManagementData w JOIN w.report r WHERE r.reportId = :reportId")
    Optional<WasteManagementData> findByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM WasteManagementData w JOIN w.report r WHERE r.reportId = :reportId")
    boolean existsByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT w FROM WasteManagementData w WHERE w.report.reportId = :reportId")
    Optional<WasteManagementData> findByReportIdWithCollections(@Param("reportId") UUID reportId);
}
