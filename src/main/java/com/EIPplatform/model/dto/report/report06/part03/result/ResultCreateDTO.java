package com.EIPplatform.model.dto.report.report06.part03.result;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultCreateDTO {
    @NotNull(message = "IS_REQUIRED")
    String productionUnitName;

    @NotNull(message = "IS_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal totalProductionOutput;

    String resultCommentary;

    String reliabilityStatement;

    String completenessStatement;

    String uncertaintyMethodology;
}