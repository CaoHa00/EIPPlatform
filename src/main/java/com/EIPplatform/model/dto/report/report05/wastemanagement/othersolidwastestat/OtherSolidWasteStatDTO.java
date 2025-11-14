package com.EIPplatform.model.dto.report.report05.wastemanagement.othersolidwastestat;

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
public class OtherSolidWasteStatDTO {
    Long otherId;
    String wasteGroupOther;
    Double volumeCy;
    String selfTreatmentMethod;
    String receiverOrg;
    Double volumePy;
}