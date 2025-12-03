package com.EIPplatform.model.dto.report.report05.airemmissionmanagement.airmonitoringexceedance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class AirMonitoringExceedanceCreateDTO {

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 255)
    String pointName;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 50)
    String pointSymbol;

    @NotNull(message = "IS_REQUIRED")
    String monitoringDate;

    @Size(max = 20)
    String longitude;

    @Size(max = 20)
    String latitude;

    @NotBlank(message = "IS_REQUIRED")
    @Size(max = 100)
    String exceededParam;

    @NotNull(message = "IS_REQUIRED")
    Double resultValue;

    @NotNull(message = "IS_REQUIRED")
    Double qcvnLimit;
}