package com.EIPplatform.model.dto.report.wastemanagement.popinventorystat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopInventoryStatDTO {
    Long popInventoryId;
    String popName;
    String casCode;
    LocalDate importDate;
    BigDecimal importVolume;
    String concentration;
    BigDecimal volumeUsed;
    BigDecimal volumeStocked;
    String complianceResult;
    LocalDateTime createdAt;
}