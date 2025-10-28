package com.EIPplatform.repository.report.wastestat;

import com.EIPplatform.model.entity.report.WasteStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WasteStatRepository extends JpaRepository<WasteStat, Integer> {
    List<WasteStat> findByReportId(UUID reportId);
    List<WasteStat> findByReportIdAndWasteType(UUID reportId, String wasteType);
}