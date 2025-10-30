package com.EIPplatform.model.dto.report.report;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportA05DTO {
    UUID reportId;
    String reportCode;
    UUID businessDetailId;
    String companyName;
    Integer reportYear;
    String reportingPeriod;
    BigDecimal completionPercentage;
    LocalDateTime createdAt;
}