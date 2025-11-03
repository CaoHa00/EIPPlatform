package com.EIPplatform.model.dto.report.wastewatermanager.autowwmonitoringstats;


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
public class AutoWWMonitoringStatsCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
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
    Double ratioReceivedDesign;

    @NotNull(message = "IS_REQUIRED")
    Double ratioErrorReceived;
}