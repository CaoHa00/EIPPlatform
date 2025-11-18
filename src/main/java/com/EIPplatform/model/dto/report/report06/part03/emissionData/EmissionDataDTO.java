package com.EIPplatform.model.dto.report.report06.part03.emissionData;

import com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData.MonthlyEmissionDataDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionDataDTO {
    UUID emissionDataId;
    String sourceCode;
    String sourceName;
    List<MonthlyEmissionDataDTO> monthlyDatas;
    BigDecimal totalAnnualData;
}