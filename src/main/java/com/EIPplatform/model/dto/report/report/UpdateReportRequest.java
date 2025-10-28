package com.EIPplatform.model.dto.report.report;

import com.EIPplatform.model.dto.report.reportfield.ReportFieldRequest;
import com.EIPplatform.model.dto.report.reportsection.ReportSectionRequest;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReportRequest implements Serializable {

    @Size(max = 100, message = "INVALID_REPORTING_PERIOD_LENGTH")
    private String reportingPeriod;

    private List<ReportFieldRequest> fields;

    private List<ReportSectionRequest> sections;
}
