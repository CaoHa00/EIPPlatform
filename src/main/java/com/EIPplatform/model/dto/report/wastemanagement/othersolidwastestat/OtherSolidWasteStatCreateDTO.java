package com.EIPplatform.model.dto.report.wastemanagement.othersolidwastestat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherSolidWasteStatCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String wasteGroupOther;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumeCy;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumePy;

    @Size(max = 255)
    String selfTreatmentMethod;

    @Size(max = 255)
    String receiverOrg;
}