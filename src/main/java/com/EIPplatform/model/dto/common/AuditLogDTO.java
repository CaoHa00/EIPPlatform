package com.EIPplatform.model.dto.common;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO implements Serializable {

    private Long auditId;
    private UUID userId;
    private String userEmail;
    private String action;
    private String entityType;
    private String entityId;
    private String details;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
}
