package com.EIPplatform.model.dto.report.wastemanagement.industrialsolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndustrialSolidWasteStatUpdateDTO {
    String wasteGroup;

    BigDecimal volumeCy;

    String receiverOrg;

    BigDecimal volumePy;
}