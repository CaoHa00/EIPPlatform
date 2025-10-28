package com.EIPplatform.repository.report.automonstat;

import com.EIPplatform.model.entity.report.AutoMonStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AutoMonStatRepository extends JpaRepository<AutoMonStat, Integer> {
    List<AutoMonStat> findByReportId(UUID reportId);
}