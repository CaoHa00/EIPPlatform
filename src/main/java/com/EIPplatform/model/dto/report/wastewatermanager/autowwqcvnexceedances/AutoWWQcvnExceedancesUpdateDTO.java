package com.EIPplatform.model.dto.report.wastewatermanager.autowwqcvnexceedances;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoWWQcvnExceedancesUpdateDTO {
    String paramName;
    Integer exceedDaysCount;
    Double qcvnLimitValue;
    Double exceedRatioPercent;
}