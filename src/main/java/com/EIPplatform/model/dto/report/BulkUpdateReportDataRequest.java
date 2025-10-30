package com.EIPplatform.model.dto.report;

import com.EIPplatform.model.dto.report.automonstat.AutoMonStatRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkUpdateReportDataRequest implements Serializable {

    @NotNull(message = "FIELD_REQUIRED")
    private UUID reportId;

    // private List<ReportFieldRequest> fields;
    // private List<ReportSectionRequest> sections;
    // private List<WasteStatRequest> wasteStats;
    private List<AutoMonStatRequest> autoMonStats;
}