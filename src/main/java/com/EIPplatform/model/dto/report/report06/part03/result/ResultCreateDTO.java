package com.EIPplatform.model.dto.report.report06.part03.result;

import jakarta.validation.constraints.DecimalMin;
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
public class ResultCreateDTO {
    String productionUnitName;

    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal totalProductionOutput;
    
    String resultCommentary;

    String reliabilityStatement;

    String intensityRatioResult;

    String completenessStatement;

    String uncertaintyMethodology;
}