package com.EIPplatform.model.dto.report.report06;

import jakarta.validation.constraints.NotNull;
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
    UUID businessDetailId;

    @NotNull(message = "Report year is required")
    Integer reportYear;

    String reportingPeriod;
}