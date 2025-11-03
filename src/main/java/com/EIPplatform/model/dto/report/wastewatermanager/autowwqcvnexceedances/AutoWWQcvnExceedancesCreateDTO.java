package com.EIPplatform.model.dto.report.wastewatermanager.autowwqcvnexceedances;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoWWQcvnExceedancesCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String paramName;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer exceedDaysCount;

    @NotNull(message = "IS_REQUIRED")
    Double qcvnLimitValue;

    @NotNull(message = "IS_REQUIRED")
    Double exceedRatioPercent;
}