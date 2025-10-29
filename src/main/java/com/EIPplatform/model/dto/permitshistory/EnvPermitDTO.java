package com.EIPplatform.model.dto.permitshistory;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvPermitDTO {
    private Long permitId;
    private String permitNumber;
    private LocalDate issueDate;
    private String issuerOrg;
    private String projectName;
    private String permitFilePath;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Integer daysUntilExpiry;
}