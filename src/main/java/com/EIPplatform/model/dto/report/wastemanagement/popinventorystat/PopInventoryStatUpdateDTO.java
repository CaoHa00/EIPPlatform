package com.EIPplatform.model.dto.report.wastemanagement.popinventorystat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopInventoryStatUpdateDTO {
    String popName;

    String casCode;

    LocalDate importDate;

    BigDecimal importVolume;

    String concentration;

    BigDecimal volumeUsed;

    BigDecimal volumeStocked;

    String complianceResult;
}