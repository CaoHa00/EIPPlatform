package com.EIPplatform.model.dto.report.wastestat;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WasteStatRequest implements Serializable {

    @NotBlank(message = "FIELD_REQUIRED")
    private String wasteType;

    @NotNull(message = "FIELD_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_VOLUME_CY")
    private BigDecimal volumeCy;

    @NotBlank(message = "FIELD_REQUIRED")
    private String receiverOrg;

    @DecimalMin(value = "0.0", inclusive = true, message = "INVALID_VOLUME_PY")
    private BigDecimal volumePy;
}
