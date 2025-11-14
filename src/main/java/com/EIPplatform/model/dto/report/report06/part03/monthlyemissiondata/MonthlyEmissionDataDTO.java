package com.EIPplatform.model.dto.report.report06.part03.monthlyemissiondata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyEmissionDataDTO {
    UUID monthlyEmissionDataId;
    Integer month;
    BigDecimal value;
    String notes;
}