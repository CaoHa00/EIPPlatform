package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautoqcvnexceedance;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoQcvnExceedanceCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String paramName;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer exceedDaysCount;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer qcvnLimitValue;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal exceedRatioPercent;
}