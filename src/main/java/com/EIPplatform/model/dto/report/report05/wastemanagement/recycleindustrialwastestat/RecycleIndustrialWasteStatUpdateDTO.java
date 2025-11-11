package com.EIPplatform.model.dto.report.report05.wastemanagement.recycleindustrialwastestat;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecycleIndustrialWasteStatUpdateDTO {
    String transferOrg;

    BigDecimal volumeCy;

    String wasteTypeDesc;

    BigDecimal volumePy;
}