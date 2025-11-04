package com.EIPplatform.model.dto.report.wastemanagement.selftreatedhwstat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SelfTreatedHwStatDTO {
    Long selfTreatedId;
    String wasteName;
    String hwCode;
    BigDecimal volumeKg;
    String selfTreatmentMethod;
}