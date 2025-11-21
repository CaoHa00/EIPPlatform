package com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat;

import lombok.*;
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
