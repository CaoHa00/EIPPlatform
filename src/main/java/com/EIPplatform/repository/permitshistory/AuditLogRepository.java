package com.EIPplatform.repository.permitshistory;

import com.EIPplatform.model.entity.permitsHistory.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /**
     * Tìm audit logs theo user
     */
    List<AuditLog> findByUserIdOrderByTimestampDesc(UUID userId);

    /**
     * Tìm audit logs theo entity
     */
    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(
            String entityType, String entityId
    );

    /**
     * Tìm audit logs với filters
     */
    @Query("SELECT al FROM AuditLog al WHERE " +
            "(:userId IS NULL OR al.userId = :userId) " +
            "AND (:action IS NULL OR al.action = :action) " +
            "AND (:entityType IS NULL OR al.entityType = :entityType) " +
            "AND (:fromDate IS NULL OR al.timestamp >= :fromDate) " +
            "AND (:toDate IS NULL OR al.timestamp <= :toDate) " +
            "ORDER BY al.timestamp DESC")
    List<AuditLog> findWithFilters(
            @Param("userId") UUID userId,
            @Param("action") String action,
            @Param("entityType") String entityType,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );
}