package com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat;

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
public class RecycleIndustrialWasteStatDTO {
    Long recycleId;
    String transferOrg;
    Double volumeCy;
    String unitCy;
    String wasteTypeDesc;
    Double volumePy;
    String unitPy;
}