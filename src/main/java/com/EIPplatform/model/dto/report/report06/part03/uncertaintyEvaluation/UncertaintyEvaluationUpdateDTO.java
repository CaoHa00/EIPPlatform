package com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UncertaintyEvaluationUpdateDTO {
    String adUncertaintyLevel;
    String efUncertaintyLevel;
}