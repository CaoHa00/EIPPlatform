package com.EIPplatform.model.dto.report.report06.part03.monthlyemissiondata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
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