package com.EIPplatform.model.dto.report.report06.part03.result;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultDTO {
    UUID resultId;
    String productionUnitName;
    BigDecimal totalProductionOutput;
    BigDecimal intensityRatioResult; // auto-calculated, read-only
    String resultCommentary;
    String reliabilityStatement;
    String completenessStatement;
    String uncertaintyMethodology;
}