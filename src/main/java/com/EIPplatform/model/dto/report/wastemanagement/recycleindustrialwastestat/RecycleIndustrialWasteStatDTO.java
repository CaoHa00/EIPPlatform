package com.EIPplatform.model.dto.report.wastemanagement.recycleindustrialwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecycleIndustrialWasteStatDTO {
    Long recycleId;
    String transferOrg;
    BigDecimal volumeCy;
    String wasteTypeDesc;
    BigDecimal volumePy;
    LocalDateTime createdAt;
}