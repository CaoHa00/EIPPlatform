package com.EIPplatform.model.entity.permitsHistory;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_entity_type", columnList = "entity_type"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType;

    @Column(name = "entity_id", nullable = false, length = 255)
    private String entityId;

    @Column(name = "details", columnDefinition = "NVARCHAR(MAX)")
    private String details;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}