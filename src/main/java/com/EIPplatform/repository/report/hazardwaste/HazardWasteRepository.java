package com.EIPplatform.repository.report.hazardwaste;

import com.EIPplatform.model.entity.report.HazardWaste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HazardWasteRepository extends JpaRepository<HazardWaste, Integer> {
    List<HazardWaste> findByReportSectionSectionId(UUID sectionId);
    List<HazardWaste> findByReportId(UUID reportId);
}