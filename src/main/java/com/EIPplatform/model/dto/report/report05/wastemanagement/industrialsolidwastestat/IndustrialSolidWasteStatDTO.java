package com.EIPplatform.model.dto.report.report05.wastemanagement.industrialsolidwastestat;

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
public class IndustrialSolidWasteStatDTO {
    Long industrialId;
    String wasteGroup;
    Double volumeCy;
    String unitCy;
    String receiverOrg;
    Double volumePy;
    String unitPy;
}