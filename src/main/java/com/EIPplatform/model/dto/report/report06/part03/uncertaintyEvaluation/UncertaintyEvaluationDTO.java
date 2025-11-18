package com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UncertaintyEvaluationDTO {
    UUID uncertaintyEvaluationId;
    String sourceCode; // read-only, auto-fill
    String sourceName; // read-only, auto-fill
    String adUncertaintyLevel;
    String efUncertaintyLevel;
    String combinedUncertainty; // auto-calculated, read-only
}