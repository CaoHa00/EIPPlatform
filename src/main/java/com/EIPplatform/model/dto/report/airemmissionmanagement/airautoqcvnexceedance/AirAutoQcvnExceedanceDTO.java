package com.EIPplatform.model.dto.report.airemmissionmanagement.airautoqcvnexceedance;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoQcvnExceedanceDTO {

    Long id;

    String paramName;

    Integer exceedDaysCount;

    Integer qcvnLimitValue;

    BigDecimal exceedRatioPercent;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}