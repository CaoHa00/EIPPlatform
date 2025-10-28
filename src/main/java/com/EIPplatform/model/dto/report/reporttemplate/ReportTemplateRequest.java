package com.EIPplatform.model.dto.report.reporttemplate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportTemplateRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 50, message = "MAX_LENGTH_EXCEEDED")
    private String templateCode;

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 255, message = "MAX_LENGTH_EXCEEDED")
    private String templateName;

    @NotNull(message = "FIELD_REQUIRED")
    private Integer templateVersion;

    @Size(max = 5000, message = "MAX_LENGTH_EXCEEDED")
    private String schemaDefinition;

    @NotNull(message = "FIELD_REQUIRED")
    private Boolean isActive;

    private LocalDate effectiveFrom;
}