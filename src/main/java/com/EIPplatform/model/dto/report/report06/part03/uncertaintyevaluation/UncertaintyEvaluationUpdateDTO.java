package com.EIPplatform.model.dto.report.report06.part03.uncertaintyevaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
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