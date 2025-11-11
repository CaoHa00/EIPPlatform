package com.EIPplatform.model.dto.report.report05.wastemanagement.domesticsolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DomesticSolidWasteStatDTO {
    Long domesticId;
    String wasteTypeName;
    BigDecimal volumeCy;
    String receiverOrg;
    BigDecimal volumePy;
}