package com.EIPplatform.model.dto.report.automonstat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoMonStatRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    private String paramName;

    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 0, message = "INVALID_VAL_DESIGN")
    private Integer valDesign;

    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 0, message = "INVALID_VAL_RECEIVED")
    private Integer valReceived;

    @NotNull(message = "FIELD_REQUIRED")
    @Min(value = 0, message = "INVALID_VAL_ERROR")
    private Integer valError;
}
