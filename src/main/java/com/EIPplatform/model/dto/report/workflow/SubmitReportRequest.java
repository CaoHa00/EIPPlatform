package com.EIPplatform.model.dto.report.workflow;

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
public class SubmitReportRequest implements Serializable {

    @NotNull(message = "FIELD_REQUIRED")
    private UUID reportId;

    @Size(max = 2000, message = "MAX_LENGTH_EXCEEDED")
    private String notes;
}
