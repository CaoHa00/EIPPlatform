package com.EIPplatform.repository.report;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.report.ReportStatus;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long>{
    Optional<ReportStatus> findById(Long id);
    void deleteById(Long id);

    // Kiểm tra tồn tại theo ID
    boolean existsByReportStatusId(Long id);

    // Tìm theo tên status
    Optional<ReportStatus> findByReportStatusName(String reportStatusName);

    // Kiểm tra tên status đã tồn tại chưa
    boolean existsByReportStatusName(String reportStatusName);
}
