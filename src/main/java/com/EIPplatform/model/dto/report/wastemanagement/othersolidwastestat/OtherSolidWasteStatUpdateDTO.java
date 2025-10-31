package com.EIPplatform.model.dto.report.wastemanagement.othersolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherSolidWasteStatUpdateDTO {
    String wasteGroupOther;

    BigDecimal volumeCy;

    String selfTreatmentMethod;

    String receiverOrg;

    BigDecimal volumePy;
}