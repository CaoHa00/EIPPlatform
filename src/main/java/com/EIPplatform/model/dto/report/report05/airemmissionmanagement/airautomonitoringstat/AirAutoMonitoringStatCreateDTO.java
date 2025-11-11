package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airautomonitoringstat;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirAutoMonitoringStatCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String paramName;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer valDesign;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer valReceived;

    @NotNull(message = "IS_REQUIRED")
    @Min(value = 0)
    Integer valError;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal ratioReceivedDesign;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal ratioErrorReceived;
}