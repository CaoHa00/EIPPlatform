package com.EIPplatform.model.dto.report.report;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateReportRequest implements Serializable {
    @NotNull(message = "FIELD_REQUIRED")
    private UUID businessDetailId;

    @NotNull(message = "FIELD_REQUIRED")
    private Integer reportTypeId;

    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 2000, message = "INVALID_REPORT_YEAR_MIN")
    @Max(value = 2100, message = "INVALID_REPORT_YEAR_MAX")
    private Integer reportYear;

    @Size(max = 100, message = "INVALID_REPORTING_PERIOD_LENGTH")
    private String reportingPeriod;
}
