package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoQcvnExceedanceUpdateDTO {

    String paramName;

    Integer exceedDaysCount;

    Integer qcvnLimitValue;

    BigDecimal exceedRatioPercent;
}