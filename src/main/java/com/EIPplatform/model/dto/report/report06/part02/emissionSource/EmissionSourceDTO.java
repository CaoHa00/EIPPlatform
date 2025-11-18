package com.EIPplatform.model.dto.report.report06.part02.emissionSource;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionSourceDTO {
    UUID emissionSourceId;
    Integer sourceScope;
    String sourceCategory;
    String sourceName;
    String sourceCode;
    String sourceDescription;
    BigDecimal ghgEmitted;
    String dataInputSource;
    String sourceUnit;
}