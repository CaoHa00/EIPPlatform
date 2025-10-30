package com.EIPplatform.model.dto.report.report;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReportRequest implements Serializable {

    @Size(max = 100, message = "INVALID_REPORTING_PERIOD_LENGTH")
    private String reportingPeriod;

    // private List<ReportFieldRequest> fields;

    // private List<ReportSectionRequest> sections;
}
