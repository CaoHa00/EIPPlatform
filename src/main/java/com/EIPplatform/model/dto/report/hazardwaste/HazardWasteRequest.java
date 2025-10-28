package com.EIPplatform.model.dto.report.hazardwaste;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HazardWasteRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    private String wasteName;

    @NotBlank(message = "FIELD_REQUIRED")
    private String hwCode;

    @NotNull(message = "FIELD_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_VOLUME_CY")
    private BigDecimal volumeCy;

    @NotBlank(message = "FIELD_REQUIRED")
    private String treatmentMethod;

    private String receiverOrg;

    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_VOLUME_PY")
    private BigDecimal volumePy;

    private String sectionType;
}