package com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyEmissionDataDTO {
    UUID id;
    Integer month;
    BigDecimal value;
    String notes;
}