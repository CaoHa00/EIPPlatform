package com.EIPplatform.repository.report.reportstatus;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.report.ReportStatus;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Integer> {

    // Kiểm tra tồn tại theo ID (đã có sẵn từ JpaRepository: existsById)

    // Tìm theo tên status
    Optional<ReportStatus> findByStatusName(String statusName);

    // Kiểm tra tên status đã tồn tại chưa
    boolean existsByStatusName(String statusName);

    // Tìm theo status code (bổ sung cho service)
    Optional<ReportStatus> findByStatusCode(String statusCode);

    // Kiểm tra status code đã tồn tại chưa (bổ sung nếu cần)
    boolean existsByStatusCode(String statusCode);

}