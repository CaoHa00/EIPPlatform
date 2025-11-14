package com.EIPplatform.model.dto.report.report05.wastemanagement.popinventorystat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PopInventoryStatDTO {
    Long popInventoryId;
    String popName;
    String casCode;
    String importDate;
    Double importVolume;
    String concentration;
    Double volumeUsed;
    Double volumeStocked;
    String complianceResult;
}