package com.EIPplatform.model.dto.report.reporttype;

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
public class ReportTypeRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 255, message = "MAX_LENGTH_EXCEEDED")
    private String reportName;

    @NotNull(message = "FIELD_REQUIRED")
    private Integer reportTemplateId;

    @NotNull(message = "FIELD_REQUIRED")
    private LocalDate dueDate;

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 50, message = "MAX_LENGTH_EXCEEDED")
    private String frequency;

    @Size(max = 1000, message = "MAX_LENGTH_EXCEEDED")
    private String description;
}