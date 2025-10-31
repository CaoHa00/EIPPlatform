package com.EIPplatform.model.dto.report.wastemanagement.industrialsolidwastestat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndustrialSolidWasteStatCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String wasteGroup;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumeCy;

    @NotBlank(message = "IS_REQUIRED")
    String receiverOrg;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumePy;
}