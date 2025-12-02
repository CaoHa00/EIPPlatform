package com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomesticSolidWasteStatDTO {
    Long domesticId;
    String wasteTypeName;
    Double volumeCy;
    String unitCy;
    String receiverOrg;
    Double volumePy;
    String unitPy;
}
