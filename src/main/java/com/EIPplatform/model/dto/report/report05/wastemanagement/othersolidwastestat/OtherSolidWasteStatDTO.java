package com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherSolidWasteStatDTO {
    Long otherId;
    String wasteGroupOther;
    Double volumeCy;
    String unitCy;
    String selfTreatmentMethod;
    String receiverOrg;
    Double volumePy;
    String unitPy;
}