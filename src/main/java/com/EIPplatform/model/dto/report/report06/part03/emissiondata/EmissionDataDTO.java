package com.EIPplatform.model.dto.report.report06.part03.emissiondata;

import com.EIPplatform.model.dto.report.report06.part03.monthlyemissiondata.MonthlyEmissionDataDTO;
import com.EIPplatform.model.dto.report.report06.part03.uncertaintyevaluation.UncertaintyEvaluationDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
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

    String sourceCode;   // Từ EmissionSource (read-only)
    String sourceName;   // Từ EmissionSource (read-only)

    BigDecimal totalAnnualData; // Auto SUM từ monthlyDatas (read-only)

    @Valid
    List<@Valid MonthlyEmissionDataDTO> monthlyDatas;

    UncertaintyEvaluationDTO uncertaintyEvaluation;
}