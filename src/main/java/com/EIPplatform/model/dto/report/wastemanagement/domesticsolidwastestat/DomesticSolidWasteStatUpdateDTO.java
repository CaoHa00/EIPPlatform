package com.EIPplatform.model.dto.report.wastemanagement.domesticsolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomesticSolidWasteStatUpdateDTO {
    String wasteTypeName;

    BigDecimal volumeCy;

    String receiverOrg;

    BigDecimal volumePy;
}