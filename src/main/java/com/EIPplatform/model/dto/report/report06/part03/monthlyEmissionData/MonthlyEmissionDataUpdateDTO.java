package com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyEmissionDataUpdateDTO {
    Integer month;
    BigDecimal value;
    String notes;
}