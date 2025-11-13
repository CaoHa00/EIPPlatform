package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

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

    Double exceedRatioPercent;
}