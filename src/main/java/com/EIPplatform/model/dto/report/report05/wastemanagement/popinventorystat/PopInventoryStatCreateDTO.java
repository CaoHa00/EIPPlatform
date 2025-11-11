package com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Size(max = 50)
    String casCode;

    LocalDate importDate;

    @Size(max = 100)
    String concentration;

    @Size(max = 255)
    String complianceResult;
}