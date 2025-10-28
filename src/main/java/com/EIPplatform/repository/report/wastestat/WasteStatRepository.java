package com.EIPplatform.repository.report.wastestat;

import com.EIPplatform.model.entity.report.WasteStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WasteStatRepository extends JpaRepository<WasteStat, Integer> {

    @Query("SELECT ws FROM WasteStat ws WHERE ws.report.reportId = :reportId")
    List<WasteStat> findByReportId(@Param("reportId") UUID reportId);

    @Query("SELECT ws FROM WasteStat ws WHERE ws.report.reportId = :reportId AND ws.wasteType = :wasteType")
    List<WasteStat> findByReportIdAndWasteType(@Param("reportId") UUID reportId, @Param("wasteType") String wasteType);
}