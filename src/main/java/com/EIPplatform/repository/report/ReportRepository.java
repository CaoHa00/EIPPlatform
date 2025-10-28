package com.EIPplatform.repository.report;

import com.EIPplatform.model.entity.report.Report;
import com.EIPplatform.model.entity.report.ReportType;
import com.EIPplatform.model.entity.user.userInformation.BusinessDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {

    /**
     * ✅ CHANGED: Kiểm tra báo cáo đã tồn tại theo Business
     */
    boolean existsByBusinessDetailAndReportTypeAndReportYearAndReportingPeriod(
            BusinessDetail businessDetail,
            ReportType reportType,
            Integer reportYear,
            String reportingPeriod
    );

    /**
     * Tìm theo report code
     */
    Optional<Report> findByReportCode(String reportCode);

    /**
     * CHANGED: Tìm báo cáo của business
     */
    @Query("SELECT r FROM Report r WHERE r.businessDetail = :businessDetail " +
            "AND r.isDeleted = :isDeleted ORDER BY r.createdAt DESC")
    List<Report> findByBusinessDetailAndIsDeleted(
            @Param("businessDetail") BusinessDetail businessDetail,
            @Param("isDeleted") Boolean isDeleted
    );

    /**
     * ✅ CHANGED: Tìm báo cáo với filters
     */
    @Query("SELECT r FROM Report r WHERE " +
            "(:businessDetailId IS NULL OR r.businessDetail.bussinessDetailId = :businessDetailId) " +
            "AND (:reportYear IS NULL OR r.reportYear = :reportYear) " +
            "AND (:reportTypeId IS NULL OR r.reportType.reportTypeId = :reportTypeId) " +
            "AND (:statusId IS NULL OR r.reportStatus.statusId = :statusId) " +
            "AND (:submittedById IS NULL OR r.submittedBy.userAccountId = :submittedById) " +
            "AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<Report> findWithFilters(
            @Param("businessDetailId") UUID businessDetailId,
            @Param("reportYear") Integer reportYear,
            @Param("reportTypeId") Integer reportTypeId,
            @Param("statusId") Integer statusId,
            @Param("submittedById") UUID submittedById
    );

    /**
     * ✅ CHANGED: Đếm báo cáo theo business và status
     */
    @Query("SELECT COUNT(r) FROM Report r WHERE r.businessDetail.bussinessDetailId = :businessDetailId " +
            "AND r.reportStatus.statusCode = :statusCode AND r.isDeleted = false")
    Long countByBusinessAndStatus(
            @Param("businessDetailId") UUID businessDetailId,
            @Param("statusCode") String statusCode
    );

    /**
     * Tìm báo cáo quá hạn
     */
    @Query("SELECT r FROM Report r WHERE r.reportType.dueDate < :date " +
            "AND r.reportStatus.statusCode IN ('DRAFT', 'REJECTED') " +
            "AND r.isDeleted = false")
    List<Report> findOverdueReports(@Param("date") LocalDate date);

    /**
     * CHANGED: Tìm báo cáo sắp đến hạn theo business
     */
    @Query("SELECT r FROM Report r WHERE r.reportType.dueDate BETWEEN :startDate AND :endDate " +
            "AND r.reportStatus.statusCode = 'DRAFT' " +
            "AND r.businessDetail.bussinessDetailId = :businessDetailId " +
            "AND r.isDeleted = false")
    List<Report> findUpcomingReports(
            @Param("businessDetailId") UUID businessDetailId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Tìm version history
     */
    List<Report> findByParentReportOrderByVersionDesc(Report parentReport);

    /**
     * Tính completion percentage trung bình
     */
    @Query("SELECT AVG(r.completionPercentage) FROM Report r WHERE r.isDeleted = false")
    BigDecimal getAverageCompletionPercentage();

    /**
     * NEW: Tính completion percentage trung bình theo business
     */
    @Query("SELECT AVG(r.completionPercentage) FROM Report r WHERE r.businessDetail.bussinessDetailId = :businessDetailId AND r.isDeleted = false")
    BigDecimal getAverageCompletionPercentageByBusiness(@Param("businessDetailId") UUID businessDetailId);

    /**
     *  NEW: Tìm tất cả báo cáo của một business
     */
    @Query("SELECT r FROM Report r WHERE r.businessDetail.bussinessDetailId = :businessDetailId " +
            "AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<Report> findAllByBusinessDetailId(@Param("businessDetailId") UUID businessDetailId);

    /**
     *  NEW: Đếm tổng báo cáo của business
     */
    Long countByBusinessDetailAndIsDeleted(BusinessDetail businessDetail, Boolean isDeleted);
}