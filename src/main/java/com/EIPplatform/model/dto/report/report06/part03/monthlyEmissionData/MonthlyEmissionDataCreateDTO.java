package com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyEmissionDataCreateDTO {
    @NotNull(message = "IS_REQUIRED")
    @Min(value = 1)
    @Max(value = 12)
    Integer month;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal value;

    String notes;
}