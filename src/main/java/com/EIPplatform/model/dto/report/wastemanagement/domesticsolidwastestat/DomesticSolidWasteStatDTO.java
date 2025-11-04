package com.EIPplatform.model.dto.report.wastemanagement.domesticsolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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