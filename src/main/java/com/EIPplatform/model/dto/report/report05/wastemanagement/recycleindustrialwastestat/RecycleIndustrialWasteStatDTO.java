package com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

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
}