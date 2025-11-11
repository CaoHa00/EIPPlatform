package com.EIPplatform.model.dto.report.wastemanagement.hazardouswastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HazardousWasteStatDTO {
    Long hazardousId;
    String wasteName;
    String hwCode;
    Double volumeCy;
    String treatmentMethod;
    String receiverOrg;
    Double volumePy;
}