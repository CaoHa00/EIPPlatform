package com.EIPplatform.model.dto.report.report05.wastewatermanager.autowwqcvnexceedances;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoWWQcvnExceedancesDTO {
    Long qcvnExceedId;
    String paramName;
    Integer exceedDaysCount;
    Double qcvnLimitValue;
    Double exceedRatioPercent;
}