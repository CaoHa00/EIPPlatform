package com.EIPplatform.model.dto.report.airemmissionmanagement.airmonitoringexceedance;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AirMonitoringExceedanceCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 255)
    String pointName;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 50)
    String pointSymbol;

    @NotNull(message = "IS_REQUIRED")
    LocalDate monitoringDate;

    @Size(max = 20)
    String longitude;

    @Size(max = 20)
    String latitude;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String exceededParam;

    @NotNull(message = "IS_REQUIRED")
    BigDecimal resultValue;

    @NotNull(message = "IS_REQUIRED")
    BigDecimal qcvnLimit;
}