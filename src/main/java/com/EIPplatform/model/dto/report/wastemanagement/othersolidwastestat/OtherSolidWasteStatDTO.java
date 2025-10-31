package com.EIPplatform.model.dto.report.wastemanagement.othersolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtherSolidWasteStatDTO {
    Long otherId;
    String wasteGroupOther;
    BigDecimal volumeCy;
    String selfTreatmentMethod;
    String receiverOrg;
    BigDecimal volumePy;
    LocalDateTime createdAt;
}