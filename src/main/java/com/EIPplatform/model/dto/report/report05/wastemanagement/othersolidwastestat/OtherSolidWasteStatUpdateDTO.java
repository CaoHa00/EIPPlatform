package com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherSolidWasteStatUpdateDTO {
    String wasteGroupOther;

    Double volumeCy;

    String unitCy;

    String selfTreatmentMethod;

    String receiverOrg;

    Double volumePy;

    String unitPy;
}