package com.EIPplatform.repository.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.EIPplatform.model.entity.report.WasteWaterData;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WasteWaterRepository extends JpaRepository<WasteWaterData,Long>  {
     /**
     * Tìm dữ liệu nước thải theo reportId
     */
    @Query("""
        SELECT ww
        FROM WasteWaterData ww
        LEFT JOIN FETCH ww.report
        WHERE ww.report.reportId = :reportId
    """)
    Optional<WasteWaterData> findByReportId(@Param("reportId") UUID reportId);
    
    /**
     * Kiểm tra xem report đã có dữ liệu nước thải chưa
     */
    @Query("""
        SELECT COUNT(ww) > 0
        FROM WasteWaterData ww
        WHERE ww.report.reportId = :reportId
    """)
    boolean existsByReportId(@Param("reportId") UUID reportId);
    
    /**
     * Lấy tất cả dữ liệu nước thải (có thể dùng cho admin)
     */
    @Query("""
        SELECT ww
        FROM WasteWaterData ww
        LEFT JOIN FETCH ww.report r
        ORDER BY r.reportYear DESC
    """)
    List<WasteWaterData> findAllWithReport();
    
}
