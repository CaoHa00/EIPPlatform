package com.EIPplatform.model.dto.report.report06.part03.emissionData;
import com.EIPplatform.model.dto.report.report06.part03.monthlyEmissionData.MonthlyEmissionDataUpdateDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionDataUpdateDTO {
    List<MonthlyEmissionDataUpdateDTO> monthlyDatas;
    BigDecimal totalAnnualData;
}