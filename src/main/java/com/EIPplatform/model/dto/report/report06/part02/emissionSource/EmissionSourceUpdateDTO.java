package com.EIPplatform.model.dto.report.report06.part02.emissionSource;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmissionSourceUpdateDTO {
    Integer sourceScope;
    String sourceCategory;
    String sourceName;
    String sourceCode;
    String sourceDescription;
    String dataInputSource;
    String sourceUnit;
}