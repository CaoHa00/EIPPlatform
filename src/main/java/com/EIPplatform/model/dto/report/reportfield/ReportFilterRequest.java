package com.EIPplatform.model.dto.report.reportfield;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFilterRequest implements Serializable {

    private Integer reportYear;
    private String reportingPeriod;
    private Integer reportTypeId;
    private Integer statusId;

    private UUID businessDetailId;
    private UUID submittedById;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDir = "DESC";
}