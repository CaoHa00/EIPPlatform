package com.EIPplatform.model.dto.report.wastemanagement.popinventorystat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopInventoryStatCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String popName;

    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal importVolume;

    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumeUsed;

    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumeStocked;
}