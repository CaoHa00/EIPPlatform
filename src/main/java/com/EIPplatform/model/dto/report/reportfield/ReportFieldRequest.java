package com.EIPplatform.model.dto.report.reportfield;

import jakarta.validation.constraints.NotBlank;
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
public class ReportFieldRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    @Size(max = 255, message = "MAX_LENGTH_EXCEEDED")
    private String fieldName;

    @Size(max = 5000, message = "MAX_LENGTH_EXCEEDED")
    private String fieldValue;
}
