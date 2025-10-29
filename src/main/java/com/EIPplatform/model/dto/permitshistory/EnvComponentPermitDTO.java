package com.EIPplatform.model.dto.permitshistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvComponentPermitDTO {
    private Long permitId;
    private String permitType;
    private String projectName;
    private String permitNumber;
    private LocalDate issueDate;
    private String issuerOrg;
    private String permitFilePath;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Integer daysUntilExpiry;
}