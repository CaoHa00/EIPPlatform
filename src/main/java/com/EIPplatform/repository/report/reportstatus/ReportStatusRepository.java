package com.EIPplatform.repository.report.reportstatus;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EIPplatform.model.entity.report.ReportStatus;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, Integer> {

    Optional<ReportStatus> findByStatusName(String statusName);

    boolean existsByStatusName(String statusName);

    Optional<ReportStatus> findByStatusCode(String statusCode);

    boolean existsByStatusCode(String statusCode);

    List<ReportStatus> findAllByOrderByStatusOrderAsc();
}