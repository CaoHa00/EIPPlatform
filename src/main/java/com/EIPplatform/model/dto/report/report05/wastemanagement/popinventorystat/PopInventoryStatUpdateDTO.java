package com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.lang.Double;
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

    Double importVolume;

    String concentration;

    Double volumeUsed;

    Double volumeStocked;

    String complianceResult;
}