package com.EIPplatform.model.dto.permitshistory;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnvPermitDTO {
    Long permitId;
    String permitType;
    String permitNumber;
    String projectName;
    LocalDate issueDate;
    String issuerOrg;
    String permitFilePath;
    Boolean isActive;
    LocalDateTime createdAt;
    Integer daysUntilExpiry;
}