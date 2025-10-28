package com.EIPplatform.model.dto.report.monitorexceedance;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitorExceedanceRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    private String pointName;

    private String pointSymbol;

    @NotNull(message = "FIELD_REQUIRED")
    private LocalDate monitoringDate;

    private String longitude;
    private String latitude;

    @NotBlank(message = "FIELD_REQUIRED")
    private String exceededParam;

    @NotNull(message = "FIELD_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_RESULT_VALUE")
    private BigDecimal resultValue;

    @NotNull(message = "FIELD_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_QCVN_LIMIT")
    private BigDecimal qcvnLimit;

    private String sectionType;
}