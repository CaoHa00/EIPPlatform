package com.EIPplatform.repository.report.wastewatermanager;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.EIPplatform.model.entity.report.wastewatermanager.WasteWaterData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WasteWaterRepository extends JpaRepository<WasteWaterData, Long> {
    Optional<WasteWaterData> findByReport_ReportId(UUID reportId);

    boolean existsByReport_ReportId(UUID reportId);

    @Query("SELECT ww FROM WasteWaterData ww JOIN ww.report r WHERE r.reportId = :reportId")
    Optional<WasteWaterData> findByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT CASE WHEN COUNT(ww) > 0 THEN true ELSE false END FROM WasteWaterData ww JOIN ww.report r WHERE r.reportId = :reportId")
    boolean existsByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT ww FROM WasteWaterData ww LEFT JOIN FETCH ww.report r ORDER BY r.reportYear DESC")
    List<WasteWaterData> findAllWithReport();

    @Query("SELECT w FROM WasteWaterData w WHERE w.report.reportId = :reportId")
    Optional<WasteWaterData> findByReportIdWithCollections(@Param("reportId") UUID reportId);
}
