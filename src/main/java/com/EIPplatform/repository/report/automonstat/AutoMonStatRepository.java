package com.EIPplatform.repository.report.automonstat;

import com.EIPplatform.model.entity.report.AutoMonStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AutoMonStatRepository extends JpaRepository<AutoMonStat, Integer> {

    @Query("SELECT a FROM AutoMonStat a WHERE a.report.reportId = :reportId")
    List<AutoMonStat> findByReportId(@Param("reportId") UUID reportId);
}