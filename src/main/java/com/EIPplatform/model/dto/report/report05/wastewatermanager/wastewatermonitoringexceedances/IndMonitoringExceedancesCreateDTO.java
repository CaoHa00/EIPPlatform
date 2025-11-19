package com.EIPplatform.model.dto.report.report05.wastewatermanager.wastewatermonitoringexceedances;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndMonitoringExceedancesCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String pointName;

    @NotBlank(message = "IS_REQUIRED")
    String pointSymbol;

    @NotBlank(message = "IS_REQUIRED")
    String monitoringDate;

    String longitude;

    String latitude;

    @NotBlank(message = "IS_REQUIRED")
    String exceededParam;

    Double resultValue;

    Double qcvnLimit;
}
