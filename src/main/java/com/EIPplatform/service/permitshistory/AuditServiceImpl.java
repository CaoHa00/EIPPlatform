package com.EIPplatform.service.permitshistory;

import com.EIPplatform.model.dto.common.AuditLogDTO;
import com.EIPplatform.model.entity.permitsHistory.AuditLog;
import com.EIPplatform.repository.permitshistory.AuditLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Autowired
    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void logAction(UUID userId, String action, String entityType,
                          String entityId, String details) {
        logAction(userId, action, entityType, entityId, details, null, null);
    }

    @Override
    public void logAction(UUID userId, String action, String entityType,
                          String entityId, String details, String ipAddress, String userAgent) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .userId(userId)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .details(details)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .timestamp(LocalDateTime.now())
                    .build();

            auditLogRepository.save(auditLog);
            log.debug("Audit log created: {} {} on {}", action, entityType, entityId);
        } catch (Exception e) {
            log.error("Failed to create audit log", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getAuditLogs(Map<String, String> filters) {
        UUID userId = filters.get("userId") != null ? UUID.fromString(filters.get("userId")) : null;
        String action = filters.get("action");
        String entityType = filters.get("entityType");
        LocalDateTime fromDate = filters.get("fromDate") != null ?
                LocalDateTime.parse(filters.get("fromDate")) : null;
        LocalDateTime toDate = filters.get("toDate") != null ?
                LocalDateTime.parse(filters.get("toDate")) : null;

        List<AuditLog> auditLogs = auditLogRepository.findWithFilters(
                userId, action, entityType, fromDate, toDate
        );

        return auditLogs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getEntityAuditLogs(String entityType, String entityId) {
        List<AuditLog> auditLogs = auditLogRepository
                .findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);

        return auditLogs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> getUserAuditLogs(UUID userId) {
        List<AuditLog> auditLogs = auditLogRepository.findByUserIdOrderByTimestampDesc(userId);

        return auditLogs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AuditLogDTO toDTO(AuditLog auditLog) {
        return AuditLogDTO.builder()
                .auditId(auditLog.getAuditId())
                .userId(auditLog.getUserId())
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .details(auditLog.getDetails())
                .ipAddress(auditLog.getIpAddress())
                .userAgent(auditLog.getUserAgent())
                .timestamp(auditLog.getTimestamp())
                .build();
    }
}