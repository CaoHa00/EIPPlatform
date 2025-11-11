package com.EIPplatform.model.dto.report.report05.wastemanagement.hazardouswastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HazardousWasteStatUpdateDTO {
    String wasteName;

    String hwCode;

    BigDecimal volumeCy;

    String treatmentMethod;

    String receiverOrg;

    BigDecimal volumePy;
}