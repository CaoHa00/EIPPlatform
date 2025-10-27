package com.EIPplatform.service.permitshistory;

import com.EIPplatform.model.dto.common.AuditLogDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AuditService {

    /**
     * Log một action
     */
    void logAction(UUID userId, String action, String entityType,
                   String entityId, String details);

    /**
     * Log action với thông tin request
     */
    void logAction(UUID userId, String action, String entityType,
                   String entityId, String details, String ipAddress, String userAgent);

    /**
     * Lấy audit logs với filters
     */
    @Transactional(readOnly = true)
    List<AuditLogDTO> getAuditLogs(Map<String, String> filters);

    /**
     * Lấy audit logs của một entity
     */
    @Transactional(readOnly = true)
    List<AuditLogDTO> getEntityAuditLogs(String entityType, String entityId);

    /**
     * Lấy audit logs của user
     */
    @Transactional(readOnly = true)
    List<AuditLogDTO> getUserAuditLogs(UUID userId);
}