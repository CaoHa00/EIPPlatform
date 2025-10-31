package com.EIPplatform.model.dto.report.wastemanagement.industrialsolidwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IndustrialSolidWasteStatDTO {
    Long industrialId;
    String wasteGroup;
    BigDecimal volumeCy;
    String receiverOrg;
    BigDecimal volumePy;
    LocalDateTime createdAt;
}