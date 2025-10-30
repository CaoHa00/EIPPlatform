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
}