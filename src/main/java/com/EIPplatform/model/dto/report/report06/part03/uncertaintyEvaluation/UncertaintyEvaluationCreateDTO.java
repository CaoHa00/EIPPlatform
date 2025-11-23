package com.EIPplatform.model.dto.report.report06.part03.uncertaintyEvaluation;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UncertaintyEvaluationCreateDTO {
    @NotBlank(message = "IS_REQUIRED")
    String adUncertaintyLevel;

    @NotBlank(message = "IS_REQUIRED")
    String efUncertaintyLevel;
}