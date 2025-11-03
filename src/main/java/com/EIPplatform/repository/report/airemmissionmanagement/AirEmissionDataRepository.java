package com.EIPplatform.repository.report.airemmissionmanagement;

import com.EIPplatform.model.entity.report.airemmissionmanagement.AirEmissionData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AirEmissionDataRepository extends JpaRepository<AirEmissionData, Long> {

    Optional<AirEmissionData> findByReport_ReportId(UUID reportId);

    boolean existsByReport_ReportId(UUID reportId);

    @Query("SELECT a FROM AirEmissionData a JOIN a.report r WHERE r.reportId = :reportId")
    Optional<AirEmissionData> findByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AirEmissionData a JOIN a.report r WHERE r.reportId = :reportId")
    boolean existsByReportId(@Param("reportId") UUID reportId);
}