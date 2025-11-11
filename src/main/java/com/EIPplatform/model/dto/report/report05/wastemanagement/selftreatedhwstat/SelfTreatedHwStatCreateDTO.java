package com.EIPplatform.model.dto.report.report05.wastemanagement.selftreatedhwstat;

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
public class SelfTreatedHwStatCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String wasteName;

    @NotBlank(message = "IS_REQUIRED")
    String hwCode;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal volumeKg;

    @NotBlank(message = "IS_REQUIRED")
    String selfTreatmentMethod;
}