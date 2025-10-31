package com.EIPplatform.model.dto.report.wastemanagement.hazardouswastestat;

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
public class HazardousWasteStatCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String wasteName;

    @NotBlank(message = "IS_REQUIRED")
    String hwCode;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumeCy;

    @NotBlank(message = "IS_REQUIRED")
    String treatmentMethod;

    @NotBlank(message = "IS_REQUIRED")
    String receiverOrg;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumePy;
}