package com.EIPplatform.model.dto.report.report06.part03.uncertaintyevaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UncertaintyEvaluationDTO {
    UUID uncertaintyEvaluationId;

    String sourceCode;
    String sourceName;

    String adUncertaintyLevel;
    String efUncertaintyLevel;
    String combinedUncertainty;
}